package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.*;
import com.codecool.eshipdiary.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class RentalController {
    private static final Logger LOG = LoggerFactory.getLogger(RentalController.class);

    @Autowired
    private RentalLogRepositoryService rentalLogRepositoryService;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private ShipRepositoryService shipRepositoryService;

    @Autowired
    private ShipTypeRepositoryService shipTypeRepositoryService;

    @Autowired
    private OarRepositoryService oarRepositoryService;

    @ModelAttribute("rental")
    public RentalLog rentalLog() {
        RentalLog rentalLog = new RentalLog();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepositoryService.getUserByUserName(userName);
        rentalLog.setCaptain(user.isPresent() ? user.get() : null);
        return rentalLog;
    }

    @ModelAttribute("finalRental")
    public RentalLog finalRental() {
        return new RentalLog();
    }

    @ModelAttribute("users")
    public List<User> listUsers() {
        return (List<User>) userRepositoryService.getAllUsers();
    }

    @ModelAttribute("ships")
    public List<Ship> listShips() {
        return (List<Ship>) shipRepositoryService.getAllShips();
    }

    @ModelAttribute("shipTypes")
    public List<ShipType> listShipTypes() {
        return (List<ShipType>) shipTypeRepositoryService.getAllShipType();
    }

    @ModelAttribute("oars")
    public List<Oar> listOars() {
        return (List<Oar>) oarRepositoryService.getAllOars();
    }

    @RequestMapping(value = {"/rentals", "/rentals/**"})
    public String getRentalHistory(Model model) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("role", userRepositoryService.getUserByUserName(userName).get().getRole().getName());
        return "rentals";
    }

    @RequestMapping(value = "/rentals", method = RequestMethod.OPTIONS)
    public String rentalForm(){
        return "rental_log/rental_form";
    }

    @RequestMapping(value = "/rentals/details/{rentalId}", method = RequestMethod.OPTIONS)
    public String rentalLogDetails(@PathVariable("rentalId") Long id, Model model){
        Optional<RentalLog> rentalLog = rentalLogRepositoryService.getRentalLogById(id);
        RentalLog match = rentalLog.isPresent() ? rentalLog.get() : new RentalLog();
        model.addAttribute("rental", match);
        model.addAttribute("crewDetails", crewDetails(match));
        return "rental_log/rental_details";
    }

    @RequestMapping(value = "/rentals/final/{rentalId}", method = RequestMethod.OPTIONS)
    public String rentalLogFinalize(@PathVariable("rentalId") Long id, Model model){
        Optional<RentalLog> rentalLog = rentalLogRepositoryService.getRentalLogById(id);
        RentalLog match = rentalLog.isPresent() ? rentalLog.get() : new RentalLog();
        model.addAttribute("rentalId", match.getId());
        model.addAttribute("ship", match.getChosenShip());
        model.addAttribute("oars", match.getOars());
        model.addAttribute("comment", match.getComment());
//        model.addAttribute("change", "return changeRental(" + id + ")");
        model.addAttribute("link", "/rentals/final/transaction/" + id);
        return "rental_log/rental_finalize";
    }

    @RequestMapping(value = "/rentals/final-and-change/{rentalId}", method = RequestMethod.OPTIONS)
    public String rentalLogFinalizeAndChange(@PathVariable("rentalId") Long id, Model model){
        Optional<RentalLog> rentalLog = rentalLogRepositoryService.getRentalLogById(id);
        RentalLog match = rentalLog.isPresent() ? rentalLog.get() : new RentalLog();
        RentalLog newRental = new RentalLog();
        newRental.copyRelevantFields(match);
        model.addAttribute("rental", newRental);
        return "rental_log/rental_form";
    }

    @RequestMapping(value = "/rentals/comment/{rentalId}", method = RequestMethod.OPTIONS)
    public String rentalLogComment(@PathVariable("rentalId") Long id, Model model){
        Optional<RentalLog> rentalLog = rentalLogRepositoryService.getRentalLogById(id);
        model.addAttribute("rental", rentalLog.isPresent() ? rentalLog.get() : new RentalLog());
        model.addAttribute("submit", "return addAdminComment(" + id + ")");
        return "rental_log/rental_comment";
    }

    private String crewDetails(RentalLog rentalLog) {
        String result = "";
        for (int i=0; i < rentalLog.getCrew().size(); i++){
            User user = rentalLog.getCrew().get(i);
            String oar = rentalLog.getOars().size() > i ? rentalLog.getOars().get(i).getName() : "nincs evező";
            result += user.getLastName() + ' ' + user.getFirstName() + " (evező: " + oar + ")\n";
        }
        return result;
    }

    @RequestMapping(value = "/rentals/final/transaction/{rentalId}", method = RequestMethod.POST)
    public String finalRentalTransaction(@PathVariable("rentalId") Long id, @ModelAttribute RentalLog rental) {
        Optional<RentalLog> rentalLog = rentalLogRepositoryService.getRentalLogById(id);
        RentalLog match = rentalLog.isPresent() ? rentalLog.get() : new RentalLog();

        match.setRentalEnd(new Date());

        if (rental.getComment() != null) {
            match.setComment(rental.getComment());
        }

        if (rental.getInjuredShip() != null) {
            Ship injuredShip = rental.getInjuredShip();
            injuredShip.setActive(false);
            shipRepositoryService.save(injuredShip);
            match.setInjuredShip(injuredShip);
        }

        if (rental.getInjuredOars() != null) {
            match.setInjuredOars(rental.getInjuredOars());
            for (Oar oar : match.getInjuredOars()) {
                oar.setActive(false);
                oarRepositoryService.save(oar);
            }
        }

        match.setFinalized(true);
        rentalLogRepositoryService.save(match);
        return "redirect:/rentals";
    }

}
