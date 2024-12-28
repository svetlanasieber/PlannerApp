package com.plannerapp.controller;

import com.plannerapp.model.dto.UsersWithTasksDTO;
import com.plannerapp.model.entity.Task;
import com.plannerapp.model.entity.User;
import com.plannerapp.service.TaskService;
import com.plannerapp.service.UserService;
import com.plannerapp.util.LoggedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.Set;

@Controller
public class HomeController {

    private final LoggedUser loggedUser;
    private final UserService userService;

    private final TaskService taskService;

    public HomeController(LoggedUser loggedUser, UserService userService, TaskService taskService) {
        this.loggedUser = loggedUser;
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String index() {
        if (this.loggedUser.isLogged()) {
            return "redirect:/home";
        }

        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        if (!this.loggedUser.isLogged()) {
            return "redirect:/";
        }

        User user = this.userService.findUserById(loggedUser.getId());
        model.addAttribute("currentUser", user);

        Set<Task> assignedTasksForCurrentUser = this.userService.getAssignedTasksForCurrentUser(this.loggedUser.getId());
        model.addAttribute("usersAssignedTasks", assignedTasksForCurrentUser);

        Set<UsersWithTasksDTO> tasksForAllOtherUsers = this.userService.getTasksForAllOtherUsers(this.loggedUser.getId());
        model.addAttribute("otherUsersTasks", tasksForAllOtherUsers);

        Set<Task> getallUnassignedTasks = this.taskService.getallUnassignedTasks();
        model.addAttribute("allUnassignedTasks", getallUnassignedTasks);
        model.addAttribute("totalUnassignedTasks", getallUnassignedTasks.size());


        return "home";
    }
}
