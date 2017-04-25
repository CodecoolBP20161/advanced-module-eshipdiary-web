package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.exception.RentalCannotBeSaved;
import com.codecool.eshipdiary.model.*;
import com.codecool.eshipdiary.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Controller
public class RentalController {
    private static final Logger LOG = LoggerFactory.getLogger(RentalController.class);

    @Autowired
    private RentalLogRepositoryService rentalLogRepositoryService;

    @Autowired
    private RentalService rentalService;

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
        rentalLog.setCaptain(userRepositoryService.getCurrentUser());
        rentalLog.setClub(userRepositoryService.getCurrentUser().getClub());
        return rentalLog;
    }

    @ModelAttribute("finalRental")
    public RentalLog finalRental() {
        return new RentalLog();
    }

    @ModelAttribute("users")
    public List<User> listUsers() {
        return (List<User>) userRepositoryService.getUsersEligibleForRental();
    }

    @ModelAttribute("shipTypes")
    public List<ShipType> listShipTypes() {
        return (List<ShipType>) shipTypeRepositoryService.getAllShipType();
    }

    @RequestMapping(value = {"/rentals", "/rentals/**"})
    public String getRentalHistory(Model model) {
        model.addAttribute("role", userRepositoryService.getCurrentUser().getRole().getName());
        return "rentals";
    }

    @RequestMapping(value = "/rentals", method = RequestMethod.OPTIONS)
    public String rentalForm(Model model) {
        model.addAttribute("submit", "return saveRental()");
        return "rental_log/rental_form";
    }

    @RequestMapping(value = "/rentals/details/{rentalId}", method = RequestMethod.OPTIONS)
    public String rentalLogDetails(@PathVariable("rentalId") Long id, Model model) {
        RentalLog originalRental = getRentalLogById(id);
        model.addAttribute("rental", originalRental);
        model.addAttribute("crewDetails", crewDetails(originalRental));
        return "rental_log/rental_details";
    }

    @RequestMapping(value = "/rentals/final/{rentalId}", method = RequestMethod.OPTIONS)
    public String rentalLogFinalize(@PathVariable("rentalId") Long id, Model model) {
        RentalLog originalRental = getRentalLogById(id);
        model.addAttribute("rentalId", originalRental.getId());
        model.addAttribute("ship", originalRental.getChosenShip());
        model.addAttribute("oars", originalRental.getOars());
        model.addAttribute("comment", originalRental.getComment());
        model.addAttribute("link", "/rentals/final/" + id);
        return "rental_log/rental_finalize";
    }

    @RequestMapping(value = "/rentals/comment/{rentalId}", method = RequestMethod.OPTIONS)
    public String rentalLogComment(@PathVariable("rentalId") Long id, Model model) {
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

    @RequestMapping(value = "/rentals/final/{rentalId}", method = RequestMethod.POST)
    public String finalRentalTransaction(@PathVariable("rentalId") Long id,
                                         @ModelAttribute RentalLog rentalFinalDetails) {
        LOG.debug("Trying to update RentalLog with (only non-null properties): {}", rentalFinalDetails.toString());
        RentalLog originalRental = getRentalLogById(id);
        LOG.debug("Existing rental to update is: {}", originalRental.toString());
        rentalLogRepositoryService.finalize(originalRental, rentalFinalDetails);
        return "redirect:/rentals";
    }

    @RequestMapping(value = "/rentals/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpStatus saveRental(@ModelAttribute RentalLog rentalLog) {
        LOG.debug("Trying to save RentalLog as: {}", rentalLog.toString());
        try {
            rentalService.saveIfItemsAreAvailable(rentalLog);
        } catch (RentalCannotBeSaved rentalCannotBeSaved) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/rentalEnabled")
    public @ResponseBody Boolean rentalEnabled() {
        User currentUser = userRepositoryService.getCurrentUser();
        return currentUser.getRole().getName().equals("ADMIN") || !currentUser.isOnWater();
    }

    @RequestMapping(value = "/rentals/reuse/{rentalId}", method = RequestMethod.OPTIONS)
    public String reuseRental(@PathVariable("rentalId") Long id, Model model) {
        Optional<RentalLog> rentalLog = rentalLogRepositoryService.getRentalLogById(id);
        Ship ship = rentalLog.get().getChosenShip();
        SubType subType = ship.getSubType();
        ShipType shipType = subType.getType();
        model.addAttribute("shipType", shipType.getId());
        model.addAttribute("sType", subType.getId());
        model.addAttribute("subTypes", shipType.getSubTypes());
        model.addAttribute("ships", shipRepositoryService.getAvailableShipsBySubType(subType));
        model.addAttribute("oarType", oarRepositoryService.getAvailableOarsByType(shipType));
        RentalLog newRental = rentalLog();
        rentalService.copyCurrentlyAvailableItems(rentalLog.get(), newRental);
        model.addAttribute("rental", newRental);
        model.addAttribute("submit", "return saveRental()");
        return "rental_log/rental_form";
    }

    private RentalLog getRentalLogById(Long id) {
        Optional<RentalLog> rentalLog = rentalLogRepositoryService.getRentalLogById(id);
        return rentalLog.isPresent() ? rentalLog.get() : new RentalLog();
    }
}
