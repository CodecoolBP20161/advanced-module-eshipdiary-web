package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.ShipSize;
import com.codecool.eshipdiary.model.ShipType;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.ShipRepositoryService;
import com.codecool.eshipdiary.service.ShipSizeRepositoryService;
import com.codecool.eshipdiary.service.ShipTypeRepositoryService;
import com.codecool.eshipdiary.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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

    @Autowired
    ShipTypeRepositoryService shipTypeRepositoryService;

    @ModelAttribute("users")
    public List<User> listUsers() {
        return (List<User>) userRepositoryService.getAllUsers();
    }

    @ModelAttribute("shipSizes")
    public List<ShipSize> listSizes() {
        return (List<ShipSize>) shipSizeRepositoryService.getAllShipSize();
    }

    @ModelAttribute("shipTypes")
    public List<ShipType> listTypes() {
        return (List<ShipType>) shipTypeRepositoryService.getAllShipType();
    }

    @RequestMapping(value = {"/admin/ships", "/admin/ships/**"})
    public String getShipTable() {
        return "ships";
    }

    @RequestMapping(value = "/admin/ships/{shipId}", method = RequestMethod.OPTIONS)
    public String updateShipForm(@PathVariable("shipId") Long id, Model model){
        Optional<Ship> ship = shipRepositoryService.getShipById(id);
        model.addAttribute("ship", ship.isPresent() ? ship.get() : new Ship());
        model.addAttribute("validate", "return validateShip(" + id + ")");
        return "ships/ship_form";
    }

    @RequestMapping(value = "/admin/ships/user/{userId}", method = RequestMethod.OPTIONS)
    public String createShip(@PathVariable("userId") Long id, Model model){
        Optional<User> user = userRepositoryService.getUserById(id);
        Ship ship = new Ship();
        ship.setOwner(user.isPresent() ? user.get() : new User());
        model.addAttribute("ship", ship);
        model.addAttribute("validate", "return validateShip(0)");
        return "ships/ship_form";
    }

    @RequestMapping(value = "/admin/ships/{shipId}", method = RequestMethod.POST)
    public String updateShip(@PathVariable("shipId") Long id, @ModelAttribute("ship") @Valid Ship ship, BindingResult result, Model model) {
        model.addAttribute("validate", "return validateShip(" + id + ")");
        if(result.hasErrors()) {
            LOG.error("Error while trying to update ship: " + result.getFieldErrors());
        } else {
            model.addAttribute("submit", "return submitShip(" + id + ")");
        }
        return "ships/ship_form";
    }

    @RequestMapping("/shipsByType")
    public @ResponseBody List<Ship> getShipsByShipType(@RequestParam("typeId") Long id) {
        Optional<ShipType> type = shipTypeRepositoryService.getShipTypeById(id);
        List<Ship> shipsByType = new ArrayList<>();
        if (id == 0) {
            shipRepositoryService.getAllShips().forEach(shipsByType::add);
        } else if (type.isPresent()) {
            shipRepositoryService.getAllShipsByType(type.get()).forEach(shipsByType::add);
        }
        return shipsByType;
    }

    @RequestMapping("/isShipCoxed")
    public @ResponseBody Boolean isShipCoxed(@RequestParam("shipId") Long id) {
        Optional<Ship> ship = shipRepositoryService.getShipById(id);
        return ship.isPresent() && ship.get().isCoxed();
    }
}
