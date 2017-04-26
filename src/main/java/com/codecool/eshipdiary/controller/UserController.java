package com.codecool.eshipdiary.controller;


import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.ShipType;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.ShipRepository;
import com.codecool.eshipdiary.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@Controller
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private ShipTypeRepositoryService shipTypeRepositoryService;

    @Autowired
    private ShipRepositoryService shipRepositoryService;

    @ModelAttribute("allShipsByShipType")
    public Map<ShipType, List<Ship>> getAllShipsByShipType() {
        Map<ShipType, List<Ship>> shipsByShipType = new HashMap<>();

        for(ShipType shipType: shipTypeRepositoryService.getAllShipType()) {
            shipsByShipType.put(shipType, (List<Ship>) shipRepositoryService.getAllShipsByType(shipType));
        }

        return shipsByShipType;
    }

    @ModelAttribute("allShips")
    public List<Ship> getAllShips() {
        return (List<Ship>) shipRepositoryService.getAllShips();
    }

    @RequestMapping(value = "/admin/users")
    public String getUserTable(Model model) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("current", userRepositoryService.getUserByUserName(userName).get().getId());
        return "users";
    }

    @RequestMapping(value = "/admin/users/{userId}")
    public String updateUser(@PathVariable("userId") Long id, Model model){
        Optional<User> match = userRepositoryService.getUserById(id);
        model.addAttribute("user", match.isPresent() ? match.get() : new User());
        model.addAttribute("validate", "return validateForm(" + id + ")");
        return "users/user_form";
    }

    @RequestMapping(value = "/admin/users/{userId}", method = RequestMethod.POST)
    public String saveUser(@PathVariable("userId") Long id, @ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        model.addAttribute("validate", "return validateForm(" + id + ")");
        if(userRepositoryService.getUserByUserName(user.getUserName())
                .filter(u -> !u.getId().equals(id))
                .isPresent()){
            result.addError(new FieldError(
                    "user",
                    "userName",
                    user.getUserName(),
                    true,
                    null,
                    null,
                    "A megadott felhasználónév már foglalt"));
        }
        if(userRepositoryService.getUserByEmailAddress(user.getEmailAddress())
                .filter(u -> !u.getId().equals(id))
                .isPresent()){
            result.addError(new FieldError(
                    "user",
                    "emailAddress",
                    user.getEmailAddress(),
                    true,
                    null,
                    null,
                    "A megadott email cím már foglalt"));
        }
        if(result.hasErrors()) {
            LOG.error("Error while trying to update user: " + result.getFieldErrors());
        } else {
            model.addAttribute("submit", "return submitForm(" + id + ")");
        }
        return "users/user_form";
    }

    @RequestMapping(value = "/admin/users/{userId}/enable_ships")
    public String getShipWhitelistingForm(@PathVariable("userId") Long id, Model model) {
        User user = userRepositoryService.getUserById(id).get();
        model.addAttribute("user", user);
        return "users/ship_whitelisting_form";
    }

    @RequestMapping(value = "/admin/users/{userId}/enable_ships", method = RequestMethod.POST)
    public String enableShips(@PathVariable("userId") Long userId, @ModelAttribute("user") User user) {
        User target = userRepositoryService.getUserById(userId).get();
        target.setEnabledShips(user.getEnabledShips());
        userRepositoryService.save(target);
        return "redirect:/admin/users";
    }

    @RequestMapping("/removecox")
    public @ResponseBody HashMap<Long, String> removeCox(@RequestParam("userId") Long id) {
        HashMap<Long, String> users = new HashMap<>();
        userRepositoryService
                .getUsersEligibleForRental()
                .forEach(u -> users.put(u.getId(), u.getLastName() + ' ' + u.getFirstName()));
        users.remove(id);
        return users;
    }

}
