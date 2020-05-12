
package org.springframework.samples.flatbook.model.enums;

public enum SaveType {
	EDIT("edit"), NEW("new");

	private final String type;


	SaveType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
