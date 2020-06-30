package com.example.januush.todolistapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends BaseAuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "Task description must not be null")
	private String description;
	private boolean done;
	private LocalDateTime deadline;

	Task() {
		// Empty constructor used by Hibernate to create single Entity
	}

	public int getId() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	void setDescription(String description) {
		this.description = description;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	void setDeadline(final LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public void updateFrom(final Task source) {
		description = source.description;
		done = source.done;
		deadline = source.deadline;
	}
}
