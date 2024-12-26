package com.plannerapp.model.dto;

import com.plannerapp.model.entity.User;

import java.util.HashSet;
import java.util.Set;

public class UsersWithTasksDTO {

    private Long id;

    private String username;

    private Set<TaskDTO> tasks;

    public UsersWithTasksDTO() {
        this.tasks = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public UsersWithTasksDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UsersWithTasksDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Set<TaskDTO> getOffers() {
        return tasks;
    }

    public UsersWithTasksDTO setOffers(Set<TaskDTO> tasks) {
        this.tasks = tasks;
        return this;
    }
}
