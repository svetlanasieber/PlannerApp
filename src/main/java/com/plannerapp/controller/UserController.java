package com.plannerapp.controller;

import com.plannerapp.model.dto.LoginDTO;
import com.plannerapp.model.dto.RegistrationDTO;
import com.plannerapp.service.UserService;
import com.plannerapp.util.LoggedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private final LoggedUser loggedUser;

    private final UserService userService;

    public UserController(LoggedUser loggedUser, UserService userService) {
        this.loggedUser = loggedUser;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        if (this.loggedUser.isLogged()) {
            return "redirect:/home";
        }

        return "login";
    }

    @PostMapping("/login")
    public String loginConfirm(@Valid LoginDTO loginDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("loginDTO", loginDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.loginDTO", result);
            return "redirect:/users/login";
        }

        boolean validCredentials = this.userService.checkCredentials(loginDTO.getUsername(), loginDTO.getPassword());

        if (!validCredentials) {
            redirectAttributes
                    .addFlashAttribute("loginDTO", loginDTO)
                    .addFlashAttribute("validCredentials", false);
            return "redirect:/users/login";
        }

        this.userService.login(loginDTO.getUsername());

        return "redirect:/home";
    }

    @GetMapping("/register")
    public String register() {
        if (this.loggedUser.isLogged()) {
            return "redirect:/home";
        }

        return "register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid RegistrationDTO registrationDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            result.addError(
                    new FieldError(
                            "differentConfirmPassword",
                            "confirmPassword",
                            "Passwords must be the same."));
        }

        if (result.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("registrationDTO", registrationDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.registrationDTO", result);

            return "redirect:/users/register";
        }

        this.userService.register(registrationDTO);
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout() {
        if (!this.loggedUser.isLogged()) {
            return "redirect:/users/login";
        }

        this.userService.logout();
        return "redirect:/";
    }


    @ModelAttribute
    public RegistrationDTO registrationDTO() {
        return new RegistrationDTO();
    }

    @ModelAttribute
    public LoginDTO loginDTO() {
        return new LoginDTO();
    }

    @ModelAttribute
    public void addValidCredentialsAttribute(Model model) {
        model.addAttribute("validCredentials");
    }
}
