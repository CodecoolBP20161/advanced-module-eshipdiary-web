package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.*;
import com.codecool.eshipdiary.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
public class RentalController {
    private static final Logger LOG = LoggerFactory.getLogger(RentalController.class);

    @Autowired
    private RentalLogRepositoryService rentalLogRepositoryService;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private ShipRepositoryService shipRepositoryService;

    @Autowired
    private ShipTypeRepositoryService shipTypeRepositoryService;

    @Autowired
    private OarRepositoryService oarRepositoryService;

    @ModelAttribute("rental")
    public RentalLog rentalLog() {
        RentalLog rentalLog = new RentalLog();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepositoryService.getUserByUserName(userName);
        rentalLog.setCaptain(user.isPresent() ? user.get() : null);
        return rentalLog;
    }

    @ModelAttribute("users")
    public List<User> listUsers() {
        return (List<User>) userRepositoryService.getAllUsers();
    }

    @ModelAttribute("ships")
    public List<Ship> listShips() {
        return (List<Ship>) shipRepositoryService.getAllShips();
    }

    @ModelAttribute("shipTypes")
    public List<ShipType> listShipTypes() {
        return (List<ShipType>) shipTypeRepositoryService.getAllShipType();
    }

    @ModelAttribute("oars")
    public List<Oar> listOars() {
        return (List<Oar>) oarRepositoryService.getAllOars();
    }

    @RequestMapping("/rentals")
    public String getRentalHistory(Model model) {
        model.addAttribute("rental");
        model.addAttribute("users");
        model.addAttribute("ships");
        model.addAttribute("shipTypes");
        model.addAttribute("oars");
        return "rentals";
    }

    @RequestMapping(value = "/rentals", method = RequestMethod.OPTIONS)
    public String saveOrUpdateRentalForm(Model model){
        LOG.info("Saving rental form");
        model.addAttribute("rental");
        model.addAttribute("validate", "return validateRentalLog()");
        return "rental_log/rental_form";
    }

    @RequestMapping(value = "rentals", method = RequestMethod.POST)
    public String updateShip(@ModelAttribute("rental") @Valid RentalLog rentalLog, BindingResult result, Model model) {
        model.addAttribute("validate", "return validateRentalLog()");

        if(result.hasErrors()) {
            LOG.error("Error while trying to update rentalLog: " + result.getFieldErrors());
        } else {
            model.addAttribute("submit", "return submitRentalLog()");
        }
        return "rental_log/rental_form";
    }
}
