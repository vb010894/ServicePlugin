package com.vbsoft.models.entity;

import lombok.Data;

import java.util.List;

/**
 * Entity model.
 *
 * @author vd.zinovev
 * @since 1.0
 * @version 1.0
 */
@Data
@SuppressWarnings("unused")
public class EntityModel {

    /**
     * Class name.
     */
    private String className;

    private String pack = "org.severstal.infocom.models";

    /**
     * Table annotation specification.
     */
    private TableSpecification table;

    /**
     * Model fields specification.
     */
    private List<FieldSpecification> fields;



    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public TableSpecification getTable() {
        return table;
    }

    public void setTable(TableSpecification table) {
        this.table = table;
    }

    public List<FieldSpecification> getFields() {
        return fields;
    }

    public void setFields(List<FieldSpecification> fields) {
        this.fields = fields;
    }

    public String getPack() {
        return pack;
    }
}
