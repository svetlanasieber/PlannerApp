package com.plannerapp.service;

import com.plannerapp.model.dto.AddTaskDTO;
import com.plannerapp.model.entity.Priority;
import com.plannerapp.model.entity.PriorityEnum;
import com.plannerapp.model.entity.Task;
import com.plannerapp.model.entity.User;
import com.plannerapp.repo.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;


    private final PriorityService priorityService;

    private final DateTimeFormatter dateTimeFormatter;

    public TaskService(TaskRepository taskRepository, UserService userService, PriorityService priorityService, DateTimeFormatter dateTimeFormatter) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.priorityService = priorityService;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public void initTestTasks() {
        if (this.taskRepository.count() != 0) {
            return;
        }

        User admin = userService.findUserById(Long.parseLong("1"));
        User test = userService.findUserById(Long.parseLong("2"));

        Task task1 = new Task();
        task1.setDueDate(LocalDate.parse("15-04-2032", dateTimeFormatter));
        task1.setPriority(priorityService.findPriority(PriorityEnum.IMPORTANT));
        task1.setDescription("Prepare presentation for TBD meeting");
        assignTaskToUser(admin, task1);

        Task task2 = new Task();
        task2.setDueDate(LocalDate.parse("20-01-2032", dateTimeFormatter));
        task2.setPriority(priorityService.findPriority(PriorityEnum.LOW));
        task2.setDescription("Test new resources");
        assignTaskToUser(test, task2);

        Task task3 = new Task();
        task3.setDueDate(LocalDate.parse("15-04-2032", dateTimeFormatter));
        task3.setPriority(priorityService.findPriority(PriorityEnum.IMPORTANT));
        task3.setDescription("Prepare presentation for TBD meeting");

        userService.saveUser(admin);
        userService.saveUser(test);
    }

    private void assignTaskToUser(User user, Task task) {
        user.getAssignedTasks().add(task);
        task.setAssignedTo(user);
        taskRepository.save(task);
    }

    public Set<Task> getallUnassignedTasks() {
        return this.taskRepository.findAllByAssignedToIsNull();
    }

    public void addTask(AddTaskDTO addTaskDTO) {
        Task task = new Task();
        Priority priority = this.priorityService.findPriority(addTaskDTO.getPriority());

        task.setDescription(addTaskDTO.getDescription());
        task.setPriority(priority);
        task.setDueDate(addTaskDTO.getDueDate());

        this.taskRepository.save(task);
    }

    public void assignTaskWithId(Long taskId, Long userId) {
        User currentUser = this.userService.findUserById(userId);
        Task taskById = this.taskRepository.findById(taskId).get();
        taskById.setAssignedTo(currentUser);
        this.taskRepository.save(taskById);
        currentUser.getAssignedTasks().add(taskById);
        this.userService.saveUser(currentUser);
    }

    public void removeTaskById(Long taskId, Long userId) {
        User user = this.userService.findUserById(userId);
        Task task = user.getAssignedTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElse(null);
        user.getAssignedTasks().remove(task);

        this.userService.saveUser(user);
        this.taskRepository.delete(task);
    }

    public void returnTask(Long taskId, Long userId) {
        Task task = this.taskRepository.findById(taskId).get();
        User user = this.userService.findUserById(userId);
        Task task1 = user.getAssignedTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElse(null);
        user.getAssignedTasks().remove(task1);
        task.setAssignedTo(null);
        this.taskRepository.save(task);
        this.userService.saveUser(user);
    }
}
