
package org.springframework.samples.flatbook.model.enums;

public enum RequestStatus {
	ACCEPTED("Accepted"), PENDING("Pending"), REJECTED("Rejected"), FINISHED("Finished"), CANCELED("Canceled");

	private final String type;


	RequestStatus(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
