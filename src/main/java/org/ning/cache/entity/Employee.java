package org.ning.cache.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * employee
 * @author 
 */
@Data
@ToString
public class Employee implements Serializable {
    private Long id;

    private String lastName;

    private String email;

    private Boolean gender;

    private Long departmentId;

    private static final long serialVersionUID = 1L;
}