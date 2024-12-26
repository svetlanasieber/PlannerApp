package com.plannerapp.model.dto;

import com.plannerapp.vallidation.UniqueUsername;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginDTO {

    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters!")
    @NotNull
    private String username;

    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    @NotNull
    private String password;

    public LoginDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
