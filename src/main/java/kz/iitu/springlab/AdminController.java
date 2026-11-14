package kz.iitu.springlab;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppUserRepository users;

    public AdminController(AppUserRepository users) {
        this.users = users;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", users.findAll());
        return "admin";
    }

    @PostMapping("/users/{id}/unlock")
    public String unlock(@PathVariable Long id) {
        users.findById(id).ifPresent(u -> {
            u.setLocked(false);
            u.setFailedAttempts(0);
            users.save(u);
        });
        return "redirect:/admin";
    }
}
