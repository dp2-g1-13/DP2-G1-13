
package org.springframework.samples.flatbook.model;

public enum SaveType {
	EDIT("edit"), NEW("new");

	private final String type;


	private SaveType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
