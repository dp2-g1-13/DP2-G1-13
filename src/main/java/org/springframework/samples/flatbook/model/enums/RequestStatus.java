
package org.springframework.samples.flatbook.model.enums;

public enum RequestStatus {
	ACCEPTED("Accepted"), PENDING("Pending"), REJECTED("Rejected");

	private final String type;


	private RequestStatus(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
