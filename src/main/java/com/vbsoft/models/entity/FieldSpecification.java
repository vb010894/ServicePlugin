package com.vbsoft.models.entity;

import lombok.Data;

/**
 * Entity fields specification.
 *
 * @author vd.zinovev
 * @since 1.0
 * @version 1.0
 */
@Data
@SuppressWarnings("unused")
public class FieldSpecification {

    /**
     * Field name.
     */
    private String name;

    /**
     * Field type.
     */
    private String type;

    private boolean id;

    public boolean getId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
