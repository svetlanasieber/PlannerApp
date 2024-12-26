package com.plannerapp.init;

import com.plannerapp.service.PriorityService;
import com.plannerapp.service.TaskService;
import com.plannerapp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FirstInit implements CommandLineRunner {

    private final UserService userService;

    private final TaskService taskService;

    private PriorityService priorityService;


    public FirstInit(UserService userService, TaskService taskService, PriorityService priorityService) {
        this.userService = userService;
        this.taskService = taskService;
        this.priorityService = priorityService;
    }

    @Override
    public void run(String... args) throws Exception {

        this.userService.initAdmin();
        this.userService.initTestUser();
        this.priorityService.initPriorities();
        this.taskService.initTestTasks();
    }
}

