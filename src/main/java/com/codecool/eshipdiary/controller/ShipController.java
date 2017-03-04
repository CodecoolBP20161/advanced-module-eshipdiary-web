package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShipController {

    @ModelAttribute("ship")
    public Ship Ship() {
        return new Ship();
    }

    @RequestMapping("/ships")
    public String getShipTable(Model model) {
        model.addAttribute("ship");
        return "ships";
    }


}
