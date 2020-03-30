
package org.springframework.samples.flatbook.model.enums;

public enum AuthoritiesType {
	ADMIN("Admin"), TENANT("Tenant"), HOST("Host");

	private final String type;


	private AuthoritiesType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
