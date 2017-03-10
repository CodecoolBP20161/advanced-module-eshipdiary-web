package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.ShipSize;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.ShipRepositoryService;
import com.codecool.eshipdiary.service.ShipSizeRepositoryService;
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

    @Autowired
    ShipSizeRepositoryService shipSizeRepositoryService;

    @ModelAttribute("users")
    public List<User> listUsers() {
        return (List<User>) userRepositoryService.getAllUsers();
    }

    @ModelAttribute("shipSizes")
    public List<ShipSize> listSizes() {
        return (List<ShipSize>) shipSizeRepositoryService.getAllShipSize();
    }

    @RequestMapping(value = {"/ships", "/ships/**"})
    public String getShipTable(Model model) {
        model.addAttribute("ship");
        return "ships";
    }

    @RequestMapping(value = "/ships/{shipId}", method = RequestMethod.OPTIONS)
    public String updateShipForm(@PathVariable("shipId") Long id, Model model){
        Optional<Ship> ship = shipRepositoryService.getShipById(id);
        model.addAttribute("ship", ship.isPresent() ? ship.get() : new Ship());
        model.addAttribute("validate", "return validateShip(" + id + ")");
        return "ships/ship_form";
    }

    @RequestMapping(value = "/ships/user/{userId}", method = RequestMethod.OPTIONS)
    public String createShip(@PathVariable("userId") Long id, Model model){
        Optional<User> user = userRepositoryService.getUserById(id);
        Ship ship = new Ship();
        ship.setOwner(user.isPresent() ? user.get() : new User());
        model.addAttribute("ship", ship);
        model.addAttribute("validate", "return validateShip(" + id + ")");
        return "ships/ship_form";
    }

    @RequestMapping(value = "ships/{shipId}", method = RequestMethod.POST)
    public String updateShip(@PathVariable("shipId") Long id, @ModelAttribute("ship") @Valid Ship ship, BindingResult result, Model model) {
        model.addAttribute("validate", "return validateShip(" + id + ")");
        if(result.hasErrors()) {
            LOG.error("Error while trying to update ship: " + result.getFieldErrors());
        } else {
            model.addAttribute("submit", "return submitShip(" + id + ")");
        }
        return "ships/ship_form";
    }
}
