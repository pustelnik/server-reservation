package pl.san.jakub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.san.jakub.model.AuthoritiesAccess;
import pl.san.jakub.model.UsersAccess;
import pl.san.jakub.model.data.Authorities;
import pl.san.jakub.model.data.Users;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Jakub on 08.11.2015.
 */

@Controller
public class UserController {

    private UsersAccess usersAccess;
    private AuthoritiesAccess authoritiesAccess;

    @Autowired
    public UserController(UsersAccess usersAccess, AuthoritiesAccess authoritiesAccess) {
        this.usersAccess = usersAccess;
        this.authoritiesAccess = authoritiesAccess;
    }

    @RequestMapping(value = "/profile/register", method = GET)
    public String showRegistrationForm() {
        return "registerForm";
    }

    @RequestMapping(value = "/profile/register", method = POST)
    public String processRegistration(@Valid Users users, Errors errors) {
        if(errors.hasErrors()) {
            return "registerForm";
        }
        usersAccess.save(users);
        authoritiesAccess.save(new Authorities(users.getUsername(), "ROLE_USER"));
        return "redirect:/profile/" + users.getUsername();
    }

    @RequestMapping(value="/profile/{username}", method=GET)
    public String showSpitterProfile(@PathVariable String username, Model model) {
        Users user = usersAccess.findByUsername(username);
        model.addAttribute(user);
        return "profile";
    }


    @RequestMapping(value = "/login", method = GET)
    public ModelAndView showProfile(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if(error != null) {
            model.addObject("error", "Invalid username and password!");
        }
        if(logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;
    }
}
