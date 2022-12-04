package com.nnk.springboot.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.GlobalPoseidonException;
import com.nnk.springboot.services.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(User bid) {
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {

        if (!result.hasErrors()) {

            User existingUser = userService.findByUsername(user.getEmail());
            if (existingUser != null) {
                result.addError(new FieldError("user", "fullname", "This user already existed in application, please add another one!"));
                return "user/add";
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    /**
     * show form to update a user. id can't be not valid because the btn to
     * display form is in row of table of all existing users.
     * 
     * @param id    id of existing user to update
     * @param model model to send user
     * @return view
     * @Throw UserNotFoundException if userService don't retrieve user
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {
            User user = userService.findById(id);
            user.setPassword("");
            model.addAttribute("user", user);
            return "user/update";
        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user, BindingResult result, Model model) {

        if (id != 0) {
            if (result.hasErrors()) {
                return "user/update";
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setId(id);
            userService.saveUser(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }

    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {
            User user = userService.findById(id);
            userService.deleteUser(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        } else {
            throw new GlobalPoseidonException(messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
            }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
