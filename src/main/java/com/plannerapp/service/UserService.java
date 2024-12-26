package com.plannerapp.service;

import com.plannerapp.model.dto.RegistrationDTO;
import com.plannerapp.model.dto.TaskDTO;
import com.plannerapp.model.dto.UserDTO;
import com.plannerapp.model.dto.UsersWithTasksDTO;
import com.plannerapp.model.entity.Task;
import com.plannerapp.model.entity.User;
import com.plannerapp.repo.UserRepository;
import com.plannerapp.util.LoggedUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class UserService {
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final LoggedUser loggedUser;

    private final HttpSession session;

    public UserService(PasswordEncoder passwordEncoder, PasswordEncoder encoder, UserRepository userRepository, LoggedUser loggedUser, HttpSession session) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.loggedUser = loggedUser;
        this.session = session;
    }

    public void initAdmin() {
        if (this.userRepository.count() != 0) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("1234"));
        admin.setEmail("admin@abv.bg");
        this.userRepository.save(admin);

    }

    public void initTestUser() {
        if (this.userRepository.count() != 0) {
            return;
        }

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword(encoder.encode("1234"));
        testUser.setEmail("test@abv.bg");
        this.userRepository.save(testUser);
    }

    public void login(String username) {
        Optional<User> user = this.userRepository.getUserByUsername(username);
        this.loggedUser.setId(user.get().getId());
        this.loggedUser.setUsername(user.get().getUsername());
    }

    public User findUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public void saveUser(User admin) {
        this.userRepository.save(admin);
    }

    public UserDTO findUserByEmail(String value) {
        Optional<User> user = this.userRepository.findByEmail(value);
        if (user.isEmpty()) {
            return null;
        }

        return this.mapUserDTO(user);
    }

    private UserDTO mapUserDTO(Optional<User> user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.get().getUsername());
        userDTO.setPassword(user.get().getPassword());
        userDTO.setEmail(user.get().getEmail());
        return userDTO;
    }

    public UserDTO findUserByUsername(String value) {
        Optional<User> user = this.userRepository.getUserByUsername(value);
        if (user.isEmpty()) {
            return null;
        }

        return this.mapUserDTO(user);
    }


    public void register(RegistrationDTO registerDTO) {
        this.userRepository.save(this.mapUser(registerDTO));
        this.login(registerDTO.getUsername());
    }

    private User mapUser(RegistrationDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(encoder.encode(registerDTO.getPassword()));
        return user;
    }

    public boolean checkCredentials(String username, String password) {
        Optional<User> user = this.userRepository.getUserByUsername(username);

        if (user.isEmpty()) {
            return false;
        }

        return encoder.matches(password, user.get().getPassword());
    }

    public void logout() {
        this.session.invalidate();
        this.loggedUser.setId(null);
        this.loggedUser.setUsername(null);
    }

    public Set<Task> getAssignedTasksForCurrentUser(Long id) {
        User userById = this.findUserById(id);
        return userById.getAssignedTasks();
    }

    public Set<UsersWithTasksDTO> getTasksForAllOtherUsers(Long id) {
        Set<User> allOtherUsers = this.userRepository.findAllByIdNot(id);

        return mapToAllOtherUsersDTO(allOtherUsers);
    }

    private Set<UsersWithTasksDTO> mapToAllOtherUsersDTO(Set<User> users) {
        return users
                .stream()
                .map(u -> {
                    UsersWithTasksDTO currentDTO = new UsersWithTasksDTO();

                    Set<Task> tasks = u.getAssignedTasks();
                    Set<TaskDTO> taskDTOS = tasks
                            .stream()
                            .map(currentTask -> {
                                TaskDTO taskDTO = new TaskDTO();
                                taskDTO
                                        .setId(currentTask.getId())
                                        .setPriority(currentTask.getPriority().getPriorityName())
                                        .setDueDate(currentTask.getDueDate())
                                        .setDescription(currentTask.getDescription());
                                return taskDTO;
                            }).collect(Collectors.toSet());

                    currentDTO
                            .setId(u.getId())
                            .setUsername(u.getUsername())
                            .setOffers(taskDTOS);
                    return currentDTO;
                })
                .collect(Collectors.toSet());
    }
}
