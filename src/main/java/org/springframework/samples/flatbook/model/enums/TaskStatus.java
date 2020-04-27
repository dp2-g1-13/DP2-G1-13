package org.springframework.samples.flatbook.model.enums;

public enum TaskStatus {
	TODO("To Do"), INPROGRESS("In Progress"), DONE("Done");

	private final String type;

	TaskStatus(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
