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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
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

    @RequestMapping("/subtypesbytype")
    public @ResponseBody HashMap<Long, String> getSubTypesByType(@RequestParam("typeId") Long id) {
        Optional<ShipType> type = shipTypeRepositoryService.getShipTypeById(id);
        HashMap<Long, String> subTypesByType = new HashMap<>();
        if (type.isPresent()) {
            subTypeRepositoryService.getAllSubTypeByShipType(type.get()).forEach(s -> subTypesByType.put(s.getId(), s.getCode()));
        }
        return subTypesByType;
    }

    @RequestMapping("/isshipcoxed")
    public @ResponseBody Boolean isShipCoxed(@RequestParam("subTypeId") Long id) {
        return subTypeRepositoryService.getSubTypeById(id).map(SubType::isCoxed).orElse(false);
    }

    @RequestMapping("/getmaxseat")
    public @ResponseBody int getMaxSeat(@RequestParam("subTypeId") Long id) {
        return subTypeRepositoryService.getSubTypeById(id).map(SubType::getMaxSeat).orElse(0);
    }
}
