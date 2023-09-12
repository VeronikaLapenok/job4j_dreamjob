package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Guest");
        }
        model.addAttribute("user", user);
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User user) {
        var savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("message", "User with this email already exists");
            return "users/register";
        }
        return "redirect:/index";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Guest");
        }
        model.addAttribute("user", user);
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Email or password entered incorrectly");
            return "users/login";
        }
        var session = request.getSession();
        session.setAttribute("user", userOptional.get());
        return "redirect:/vacancies";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Guest");
        }
        model.addAttribute("user", user);
        session.invalidate();
        return "redirect:/users/login";
    }
}
