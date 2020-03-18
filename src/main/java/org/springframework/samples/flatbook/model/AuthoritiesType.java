
package org.springframework.samples.flatbook.model;

public enum AuthoritiesType {
	ADMIN("admin"), TENNANT("tennant"), HOST("host");

	private final String type;


	private AuthoritiesType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
