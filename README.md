# Spring Boot 缓存

## 架构

<img src="https://ning-wang.oss-cn-beijing.aliyuncs.com/blog-imagesCache架构.png" alt="Cache架构" style="zoom: 67%;" />

根据JSR缓存规范，每个应用程序可以有多个缓存提供者。每个缓存提供者有不同的缓存管理器。缓存管理器中有不同的缓存域，每个域中存储着一个个的键值对，这些键值对就是缓存的实体。

## Spring 对缓存的抽象

| 概念           | 解释                                                         |
| -------------- | ------------------------------------------------------------ |
| `Cache`        | 缓存接口，定义缓存操作。实现有RedisCache，EnCacheCache，ConCurrentMapCache(默认实现)等 |
| `CacheManager` | 缓存管理器，管理各种缓存(Cache)组件                          |
| `@Cacheable`   | 主要根据方法配置，能够根据方法的请求参数对其结果进行缓存     |
| `@CacheEvit`   | 清空缓存                                                     |
| `@CachePut`    | 更新缓存，保证缓存一定会被调用，结果会被缓存起来             |
| `@EnableCache` | 开启基于注解的缓存                                           |
| `KeyGenerator` | 缓存时Key的生成策略                                          |
| `Serialize`    | 缓存时Value的序列化策略                                      |
| `@CacheConfig` | 通常在类中定义，用于抽取该类中统一的缓存配置                 |
| `@Caching`     | 定义复杂的缓存规则，可以在嵌套使用多个`@Cacheable`,`@CachePut`,`@CacheEvit` |

##  Cache注解详解

`@Cacheable`、`@CachePut`和`@CacheEvit`注解主要参数

| 注解参数                                       | 解释                                                         | 示例                                                         |
| ---------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `value`|`cacheNames`                           | 缓存名称，必须指定一个                                       | `@CacheEvict(value = "emp"`或`@CacheEvict(value = {"emp","emp1"})` |
| `key`                                          | 缓存的key，可以为空，如果需要自定义。则必须需要按照SpEL表达式编写。如果不指定,缺省方法是按照方法的所有参数进行组合 | `@Cacheable(value = "emp", key = "#lastName", unless = "#result==null")` |
| `keyGenerator`                                 | key的生成方式，默认实现 `org.springframework.cache.interceptor.SimpleKeyGenerator` 可以自定义实现，只需实现接口：`org.springframework.cache.interceptor.KeyGenerator`  注意:  Key 与 keyGenerator 不能同时定义，否则会报错 | `@Cacheable(value = "emp", keyGenerator = "customKeyGenerator")` |
| `cacheManager`                                 | 用于定义管理该缓存的cacheManager                             | `@CachePut(value = "emp",cacheManager = "cacheManager")`     |
| `condition`                                    | 缓存的条件，可以为空，支持SpEL编写，返回true或者false。只有为true才进行缓存/清除缓存，在调用方法之前之后都能判断 | `@CachePut(value = "emp", key = "#result.id",condition = "#result!=null")` |
| `unless`(只能在`Cacheable`和`@CachePut`中使用) | 用于否决缓存，不同于condition，该表达式只在方法执行之后判断。判断结果为true时不缓存，结果为false时才进行缓存 | `@Cacheable(value = "emp", key = "#lastName", unless = "#result==null")` |
| `sync`                                         | 异步模式                                                     |                                                              |
| `allEntries`(只能在`@CacheEvit`中使用)         | 是否清空所有缓存，缺省为false，如果指定为true，则方法调用后将立即清空所有缓存 | `@CacheEvict(value = "emp", key = "#id", allEntries = true, beforeInvocation = true)` |
| `beforeInvocation`(只能在`CacheEvit`中使用)    | 是否在方法执行之前就清空缓存，缺省为false，如果指定为true，则在方法还没有执行的时候就清空缓存。缺省晴空下，如果方法调用出现异常，则不会清空缓存。 | `@CacheEvict(value = "emp", key = "#id", allEntries = true, beforeInvocation = true)` |

### 缓存中可以常用的SpEL

| 名字            | 位置                 | 描述                                                         | 示例                   |
| --------------- | -------------------- | ------------------------------------------------------------ | ---------------------- |
| `methodName`    | `root object`        | 当前被调用的方法名                                           | `#root.methodName`     |
| `method`        | `root object`        | 当前被调用的方法名                                           | `#root.method.name`    |
| `target`        | `root object`        | 当前被调用的对象                                             | `#target`              |
| `targetClass`   | `root object`        | 当前被调用的对象类                                           | `#root.targetClass`    |
| `args`          | `root object`        | 当前被调用的方法的参数列表                                   | `#root.args[0]`        |
| `caches`        | `root object`        | 当前方法调用所使用的缓存列表如`@CacheEvict(value = {"emp","emp1"})` | `#root.caches[0].name` |
| `argument name` | `evaluation context` | 方法参数的名字，可以直接#参数名，也可以使用`#p0`或`#a0`、`#p0`的方式。0时索引的起始点 | `#id,#a0,#p0`          |
| `result`        | `evaluation context` | 方法执行后的返回值                                           | `#result`