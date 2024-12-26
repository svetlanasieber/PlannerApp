package com.plannerapp.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "priorities")
public class Priority extends BaseEntity{

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private PriorityEnum priorityName;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "priority")
    private Set<Task> tasks;

    public Priority() {
        this.tasks = new HashSet<>();
    }

    public PriorityEnum getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(PriorityEnum priorityName) {
        this.priorityName = priorityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
