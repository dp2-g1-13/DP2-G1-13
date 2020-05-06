
package org.springframework.samples.flatbook.model.enums;

public enum ReviewType {
	TENANT_REVIEW("TenantReview"), FLAT_REVIEW("FlatReview");

	private final String type;


	ReviewType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
