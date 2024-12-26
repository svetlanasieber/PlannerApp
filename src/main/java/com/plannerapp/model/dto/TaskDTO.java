package com.plannerapp.model.dto;

import com.plannerapp.model.entity.Priority;
import com.plannerapp.model.entity.PriorityEnum;
import com.plannerapp.model.entity.User;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

public class TaskDTO {

    private Long id;

    private String description;

    private LocalDate dueDate;

    private PriorityEnum priority;

    public TaskDTO() {
    }

    public Long getId() {
        return id;
    }

    public TaskDTO setId(Long id) {
        this.id = id;
        return this;
    }

        public String getDescription() {
        return description;
    }

    public TaskDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public PriorityEnum getPriority() {
        return priority;
    }

    public TaskDTO setPriority(PriorityEnum priority) {
        this.priority = priority;
        return this;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public TaskDTO setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }
}
