package com.plannerapp.model.dto;

import com.plannerapp.model.entity.Priority;
import com.plannerapp.model.entity.PriorityEnum;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class AddTaskDTO {

    @Size(min = 2, max = 50, message = "Description length must be between 2 and 50 characters!")
    @NotNull
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Due Date must be in future!")
    private LocalDate dueDate;

    @NotNull(message = "You must select a priority!")
    private PriorityEnum priority;

    public AddTaskDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public PriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(PriorityEnum priority) {
        this.priority = priority;
    }
}
