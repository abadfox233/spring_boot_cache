package org.ning.cache.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * department
 * @author 
 */
@Data
@ToString
public class Department implements Serializable {
    private Long id;

    private String name;

    private static final long serialVersionUID = 1L;
}