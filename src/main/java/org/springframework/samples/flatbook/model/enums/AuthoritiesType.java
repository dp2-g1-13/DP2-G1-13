
package org.springframework.samples.flatbook.model.enums;

public enum AuthoritiesType {
	ADMIN("ADMIN"), TENANT("TENANT"), HOST("HOST");

	private final String type;


	AuthoritiesType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
