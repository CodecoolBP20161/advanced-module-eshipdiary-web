package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.ShipType;
import com.codecool.eshipdiary.service.ShipTypeRepositoryService;
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
import java.util.Optional;

@Controller
public class ShipTypeController {
    private static final Logger LOG = LoggerFactory.getLogger(ShipTypeController.class);


    @Autowired
    ShipTypeRepositoryService shipTypeRepositoryService;

    @RequestMapping(value = {"/shiptypes", "/shiptypes/**"})
    public String getShipTypeTable(Model model) {
        model.addAttribute("shipType");
        return "shiptypes";
    }

    @RequestMapping(value = "/shiptypes/{shipTypeId}", method = RequestMethod.OPTIONS)
    public String updateShipType(@PathVariable("shipTypeId") Long id, Model model){
        Optional<ShipType> shipType = shipTypeRepositoryService.getShipTypeById(id);
        model.addAttribute("shipType", shipType.isPresent() ? shipType.get() : new ShipType());
        model.addAttribute("validate", "return validateShipType(" + id + ")");
        return "shiptypes/shiptype_form";
    }

    @RequestMapping(value = "/shiptypes/{shipTypeId}", method = RequestMethod.POST)
    public String saveShipType(@PathVariable("shipTypeId") Long id, @ModelAttribute("shipType") @Valid ShipType shipType, BindingResult result, Model model) {
        model.addAttribute("validate", "return validateShipType(" + id + ")");
        if(result.hasErrors()) {
            LOG.error("Error while trying to update shipType: " + result.getFieldErrors());
        } else {
            model.addAttribute("submit", "return submitShipType(" + id + ")");
        }
        return "shiptypes/shiptype_form";
    }
}
