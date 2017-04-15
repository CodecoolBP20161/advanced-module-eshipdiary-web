package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.ShipType;
import com.codecool.eshipdiary.model.SubType;
import com.codecool.eshipdiary.service.ShipTypeRepositoryService;
import com.codecool.eshipdiary.service.SubTypeRepositoryService;
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
public class SubTypeController {
    private static final Logger LOG = LoggerFactory.getLogger(SubTypeController.class);


    @Autowired
    SubTypeRepositoryService subTypeRepositoryService;

    @Autowired
    ShipTypeRepositoryService shipTypeRepositoryService;

    @ModelAttribute("shipTypes")
    public List<ShipType> listTypes() {
        return (List<ShipType>) shipTypeRepositoryService.getAllShipType();
    }

    @RequestMapping(value = "/admin/subtypes/shiptype/{shipTypeId}", method = RequestMethod.OPTIONS)
    public String createSubType(@PathVariable("shipTypeId") Long typeId, Model model){
        SubType subType =  new SubType();
        subType.setType(shipTypeRepositoryService.getShipTypeById(typeId).get());
        model.addAttribute("subType", subType);
        model.addAttribute("validate", "return validateSubType(0)");
        return "subtypes/subtype_form";
    }

    @RequestMapping(value = "/admin/subtypes/{subTypeId}", method = RequestMethod.OPTIONS)
    public String updateSubType(@PathVariable("subTypeId") Long id, Model model){
        Optional<SubType> subType = subTypeRepositoryService.getSubTypeById(id);
        model.addAttribute("subType", subType.isPresent() ? subType.get() : new SubType());
        model.addAttribute("validate", "return validateSubType(" + id + ")");
        return "subtypes/subtype_form";
    }

    @RequestMapping(value = "/admin/subtypes/{subTypeId}", method = RequestMethod.POST)
    public String saveSubType(@PathVariable("subTypeId") Long id, @ModelAttribute("subType") @Valid SubType subType, BindingResult result, Model model) {
        model.addAttribute("validate", "return validateSubType(" + id + ")");
        if(result.hasErrors()) {
            LOG.error("Error while trying to update subType: " + result.getFieldErrors());
        } else {
            model.addAttribute("submit", "return submitSubType(" + id + ")");
        }
        return "subtypes/subtype_form";
    }
}
