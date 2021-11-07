package com.vbsoft.models.entity;

import lombok.Data;

/**
 * Table annotation specification.
 *
 * @author vd.zinovev
 * @since 1.0
 * @version 1.0
 */
@Data
@SuppressWarnings("unused")
public class TableSpecification {

    /**
     * Table name.
     */
    private String name;

    /**
     * Table catalog.
     */
    private String catalog;

    /**
     * Table schema.
     */
    private String schema;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
