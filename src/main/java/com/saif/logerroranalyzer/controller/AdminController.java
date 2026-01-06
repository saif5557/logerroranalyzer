package com.saif.logerroranalyzer.controller;

import com.saif.logerroranalyzer.entity.User;
import com.saif.logerroranalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @PostMapping("/users/{id}/promote")
    public String promoteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userRepository.findById(id).ifPresent(user -> {
            user.setRole("ROLE_ADMIN");
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "User " + user.getUsername() + " promoted to ADMIN");
        });
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/demote")
    public String demoteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userRepository.findById(id).ifPresent(user -> {
            user.setRole("ROLE_USER");
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "User " + user.getUsername() + " demoted to USER");
        });
        return "redirect:/admin/users";
    }
}
