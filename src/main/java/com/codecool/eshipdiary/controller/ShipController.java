package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.ShipRepositoryService;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class ShipController {
    private static final Logger LOG = LoggerFactory.getLogger(ShipController.class);

    @Autowired
    ShipRepositoryService shipRepositoryService;

    @Autowired
    UserRepositoryService userRepositoryService;

    @ModelAttribute("ship")
    public Ship Ship() {
        return new Ship();
    }

    @ModelAttribute("users")
    public List<User> listUsers() {
        return (List<User>) userRepositoryService.getAllUsers();
    }

    @RequestMapping("/ships")
    public String getShipTable(Model model) {
        model.addAttribute("ship");
        return "ships";
    }

    @RequestMapping(value = "/ships/update/{shipId}")
    public String updateShipForm(@PathVariable("shipId") Long id, Model model){
        Optional<Ship> ship = shipRepositoryService.getShipById(id);
        model.addAttribute("ship", ship.isPresent() ? ship.get() : new Ship());
        model.addAttribute("title", ship.map(Ship::getName).orElse("Új hajó"));
        return "ships/ship_form";
    }

    @RequestMapping(value = "ships/update", method = RequestMethod.POST)
    public String updateShip(@ModelAttribute("ship") @Valid Ship ship, BindingResult result) {
        if(result.hasErrors()) {
            LOG.error("Error while trying to create a new ship: " + result.getFieldErrors());
            return "ships/ship_form";
        }
        shipRepositoryService.save(ship);
        return "redirect:/ships";
    }

    @RequestMapping(value = "/ships/delete/{shipId}", method = RequestMethod.GET)
    public String deleteShip(@PathVariable("shipId") Long id) {
        shipRepositoryService.deleteShipById(id);
        return "redirect:/ships";
    }

}
