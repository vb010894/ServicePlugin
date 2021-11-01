package com.vbsoft.Generators;

import java.util.Collections;
import java.util.List;

public class ClassSpecification {
	private String name;
	private String pack;
	private String table = "null";
	private List<FieldSpecification> fieldSpecifications;
	
	public ClassSpecification(String className, List<FieldSpecification> fieldSpecifications) {
		this.name = className;
		this.fieldSpecifications = fieldSpecifications;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPack() { return this.pack; }

	public String getName() {
		return name;
	}

	public List<FieldSpecification> getFieldSpecifications() {
		return Collections.unmodifiableList(fieldSpecifications);
	}
}
