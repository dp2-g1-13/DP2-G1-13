
package org.springframework.samples.flatbook.model;

public enum TaskStatus {
	TODO("To Do"), INPROGRESS("In Progress"), DONE("Done");

	private final String type;


	private TaskStatus(final String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}
