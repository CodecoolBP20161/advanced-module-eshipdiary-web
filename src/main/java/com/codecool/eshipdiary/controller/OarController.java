package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.service.OarRepositoryService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class OarController {
    private static final Logger LOG = LoggerFactory.getLogger(OarController.class);


    @Autowired
    OarRepositoryService oarRepositoryService;

    @Autowired
    UserRepositoryService userRepositoryService;

    @ModelAttribute("users")
    public List<User> listUsers() {
        return (List<User>) userRepositoryService.getAllUsers();
    }

    @RequestMapping(value = {"/oars", "/oars/**"})
    public String getOarTable(Model model) {
        model.addAttribute("oar");
        return "oars";
    }

    @RequestMapping(value = "/oars/update/{oarsId}", method = RequestMethod.OPTIONS)
    public String updateOar(@PathVariable("oarsId") Long id, Model model){
        Optional<Oar> oar = oarRepositoryService.getOarById(id);
        model.addAttribute("oar", oar.isPresent() ? oar.get() : new Oar());
        model.addAttribute("validate", "return validateForm()");
        return "oars/oar_form";
    }

    @RequestMapping(value = "/oars/update", method = RequestMethod.POST)
    public String saveOar(@ModelAttribute("oar") @Valid Oar oar, BindingResult result, Model model) {
        model.addAttribute("validate", "return validateForm()");
        if(result.hasErrors()) {
            LOG.error("Error while trying to update oar: " + result.getFieldErrors());
        } else {
            model.addAttribute("submit", "return submitForm()");
            oarRepositoryService.save(oar);
        }
        return "oars/oar_form";
    }

    @RequestMapping(value = "/oars/delete/{oarsId}")
    public void deleteOar(@PathVariable("oarsId") Long id, HttpServletResponse response){
        oarRepositoryService.deleteOarById(id);
        response.setStatus(200);
    }


}
