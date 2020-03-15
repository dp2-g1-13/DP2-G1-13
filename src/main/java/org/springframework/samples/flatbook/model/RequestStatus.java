
package org.springframework.samples.flatbook.model;

public enum RequestStatus {
	ACCEPTED("Accepted"), PENDING("Pending"), REJECTED("Rejected");

	private final String type;


	private RequestStatus(final String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}
