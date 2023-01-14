package com.nnk.springboot.controllers;

import java.util.Optional;

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
import com.nnk.springboot.security.AuthProvider;
import com.nnk.springboot.services.UserService;

/**
 * Controller to manage User entities specialy used by only a administrator
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    /**
     * endpoint to show the list of all existing user
     * 
     * @param model
     * @return the view of list of all user
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * endpoint to show form to save a new user.
     * 
     * @param user the user to save
     * @return the view with the form to save a new user
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";
    }

    /**
     * endoint to save a new user.
     * 
     * @param user the user retrieved from form
     * @param result     bindingresult if error in filled fields
     * @param model
     * @return the view of updated list of user or the view of form to save
     *         user if there is a error in field
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {

        if (!result.hasErrors()) {

            Optional<User> existingUser = userService.findByUsername(user.getEmail());
            if (existingUser.isPresent()) {
                result.addError(new FieldError("user", "fullname",
                        "This user already existed in application, please add another one!"));
                return "user/add";
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            // add AuthProvider to Local because save from form
            user.setAuthenticationProvider(AuthProvider.LOCAL);
            userService.saveUser(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    /**
     * endpoint to show the form to update a existing user selected from the
     * list
     * of them.
     * 
     * @param id    the id od user to update
     * @param model
     * @return the view of form to update the user with given id.
     * @throws GlobalPoseidonException if the given id is not correct ( equal to
     *                                 zero)
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {
            User user = userService.findById(id);
            user.setPassword("");
            model.addAttribute("user", user);
            return "user/update";
        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * endpoint to update the user with given id before .
     * 
     * @param id      the given id of user to update
     * @param user the user to save with updated fields from the update
     *                form
     * @param result  bindingResult if there is a error of validation in fields
     * @param model
     * @return the view of updated list of user if update was done correctly
     *         or again
     *         view of update form to show which fields are not valid with error
     *         message
     * @Throws {@link GlobalPoseidonException} if given id is not correct (=0)
     */
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
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }

    }

    /**
     * endpoint to delete user with given id
     * 
     * @param id    the given id of user to delete
     * @param model
     * @return the updated list of user with deletion done
     * @throws GlobalPoseidonException if the given id is not correct (=0)
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        if (id != 0) {
            User user = userService.findById(id);
            userService.deleteUser(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        } else {
            throw new GlobalPoseidonException(
                    messageSource.getMessage("global.exception.incorrect-id", new Object[] { new Object() {
                    }.getClass().getEnclosingMethod().getName() }, LocaleContextHolder.getLocale()));
        }
    }
}
