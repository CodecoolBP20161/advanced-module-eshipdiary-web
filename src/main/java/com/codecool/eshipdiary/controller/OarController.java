package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.ShipType;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.OarRepositoryService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class OarController {
    private static final Logger LOG = LoggerFactory.getLogger(OarController.class);


    @Autowired
    OarRepositoryService oarRepositoryService;

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    ShipTypeRepositoryService shipTypeRepositoryService;

    @ModelAttribute("users")
    public List<User> listUsers() {
        return (List<User>) userRepositoryService.getAllUsers();
    }

    @ModelAttribute("shipTypes")
    public List<ShipType> listTypes() {
        return (List<ShipType>) shipTypeRepositoryService.getAllShipType();
    }

    @RequestMapping(value = {"/admin/oars", "/admin/oars/**"})
    public String getOarTable() {
        return "oars";
    }

    @RequestMapping(value = "/admin/oars/{oarId}", method = RequestMethod.OPTIONS)
    public String updateOar(@PathVariable("oarId") Long id, Model model){
        Optional<Oar> oar = oarRepositoryService.getOarById(id);
        model.addAttribute("oar", oar.isPresent() ? oar.get() : new Oar());
        model.addAttribute("validate", "return validateOar(" + id + ")");
        return "oars/oar_form";
    }

    @RequestMapping(value = "/admin/oars/user/{userId}", method = RequestMethod.OPTIONS)
    public String createOar(@PathVariable("userId") Long id, Model model){
        Optional<User> user = userRepositoryService.getUserById(id);
        Oar oar = new Oar();
        oar.setOwner(user.isPresent() ? user.get() : new User());
        model.addAttribute("oar", oar);
        model.addAttribute("validate", "return validateOar(0)");
        return "oars/oar_form";
    }

    @RequestMapping(value = "/admin/oars/shiptype/{shipTypeId}", method = RequestMethod.OPTIONS)
    public String shipTypeOar(@PathVariable("shipTypeId") Long id, Model model){
        Oar oar = new Oar();
        oar.setType(shipTypeRepositoryService.getShipTypeById(id).get());
        model.addAttribute("oar", oar);
        model.addAttribute("validate", "return validateOar(0)");
        return "oars/oar_form";
    }

    @RequestMapping(value = "/admin/oars/{oarId}", method = RequestMethod.POST)
    public String saveOar(@PathVariable("oarId") Long id, @ModelAttribute("oar") @Valid Oar oar, BindingResult result, Model model) {
        model.addAttribute("validate", "return validateOar(" + id + ")");
        if(result.hasErrors()) {
            LOG.error("Error while trying to update oar: " + result.getFieldErrors());
        } else {
            model.addAttribute("submit", "return submitOar(" + id + ")");
        }
        return "oars/oar_form";
    }

    @RequestMapping("/availableoarsbytype")
    public @ResponseBody HashMap<Long, String> getAvailableOarsByType(@RequestParam("typeId") Long id) {
        Optional<ShipType> type = shipTypeRepositoryService.getShipTypeById(id);
        HashMap<Long, String> oarsByType = new HashMap<>();
        if (type.isPresent()) {
            oarRepositoryService.getAvailableOarsByType(type.get()).forEach(s -> oarsByType.put(s.getId(), s.getName()));
        }
        return oarsByType;
    }
}
