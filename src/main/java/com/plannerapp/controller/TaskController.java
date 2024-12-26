package com.plannerapp.controller;

import com.plannerapp.model.dto.AddTaskDTO;
import com.plannerapp.repo.TaskRepository;
import com.plannerapp.service.TaskService;
import com.plannerapp.util.LoggedUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final LoggedUser loggedUser;

    public TaskController(TaskRepository taskRepository, TaskService taskService, LoggedUser loggedUser) {
        this.taskService = taskService;
        this.loggedUser = loggedUser;
    }

    @GetMapping("/task-add")
    public String tasks() {
        if (!loggedUser.isLogged()) {
            return "redirect:/users/login";
        }

        return "task-add";
    }

    @PostMapping("/task-add")
    public String addTask(@Valid AddTaskDTO addTaskDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("addTaskDTO", addTaskDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addTaskDTO", bindingResult);

            return "redirect:task-add";
        }

        this.taskService.addTask(addTaskDTO);

        return "redirect:/home";
    }

    @GetMapping("/assign-task/{id}")
    public String assignTask(@PathVariable Long id) {
        taskService.assignTaskWithId(id, loggedUser.getId());
        return "redirect:/home";
    }

    @GetMapping("/remove/{id}")
    public String removeTask(@PathVariable Long id) {
        taskService.removeTaskById(id,loggedUser.getId());

        return "redirect:/home";
    }

    @GetMapping("/return/{id}")
    public String returnTask(@PathVariable Long id) {
        taskService.returnTask(id, loggedUser.getId());
        return "redirect:/home";
    }

    @ModelAttribute
    public AddTaskDTO addTaskDTO() {
        return new AddTaskDTO();
    }
}
