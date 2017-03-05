package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.Oar;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OarController {

    @ModelAttribute("oar")
    public Oar Oar() {
        return new Oar();
    }

    @RequestMapping("/oars")
    public String getOarTable(Model model) {
        model.addAttribute("oar");
        return "oars";
    }


}
