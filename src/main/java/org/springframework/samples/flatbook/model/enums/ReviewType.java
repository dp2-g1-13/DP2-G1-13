
package org.springframework.samples.flatbook.model.enums;

public enum ReviewType {
	TENANT_REVIEW("tenantReview"), FLAT_REVIEW("flatReview");

	private final String type;


	private ReviewType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
