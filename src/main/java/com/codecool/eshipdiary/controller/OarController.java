package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.service.OarRepositoryService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class OarController {
    private static final Logger LOG = LoggerFactory.getLogger(OarController.class);


    @Autowired
    OarRepositoryService oarRepositoryService;

    @RequestMapping("/oars")
    public String getOarTable(Model model) {
        model.addAttribute("oar");
        return "oars";
    }

    @RequestMapping(value = "/oars/update/{oarsId}")
    public String updateOar(@PathVariable("oarsId") Long id, Model model){
        Optional<Oar> oar = oarRepositoryService.getOarById(id);
        model.addAttribute("oar", oar.isPresent() ? oar.get() : new Oar());
        model.addAttribute("title", oar.map(Oar::getName).orElse("Új evező"));
        return "oars/oar_form";
    }

    @RequestMapping(value = "/oars/update", method = RequestMethod.POST)
    public String saveOar(@ModelAttribute("oar") @Valid Oar oar, BindingResult result) {
        if(result.hasErrors()) {
            LOG.error("Error while trying to update oar: " + result.getFieldErrors());
            return "oars/oar_form";
        }
        oarRepositoryService.save(oar);
        return "redirect:/oars";
    }

    @RequestMapping(value = "/oars/delete/{oarsId}")
    public void deleteOar(@PathVariable("oarsId") Long id, HttpServletResponse response){
        oarRepositoryService.deleteOarById(id);
        response.setStatus(200);
    }


}
