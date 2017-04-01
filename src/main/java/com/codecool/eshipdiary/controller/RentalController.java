package com.codecool.eshipdiary.controller;

import com.codecool.eshipdiary.model.*;
import com.codecool.eshipdiary.service.*;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
        model.addAttribute("end", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));
        model.addAttribute("change", "return changeRental(" + id + ")");
        model.addAttribute("submit", "return submitFinalRentalLog()");
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


    @RequestMapping(value = "/rentals/final/transaction", produces = "application/json")
    public @ResponseBody HashMap<String, String> finalRentalTransaction(@ModelAttribute("form") String form)
            throws ParseException {
        LOG.info("Form data: {}", form);

        HashMap<String, String> returnData = new HashMap<>();
        JsonObject formObject = new Gson().fromJson(form, JsonObject.class);

        //load rental
        Long id = formObject.get("rentalId").getAsLong();
        Optional<RentalLog> rentalOptional = rentalLogRepositoryService.getRentalLogById(id);
        RentalLog rental = rentalOptional.isPresent() ? rentalOptional.get() : null;
        assert rental != null;

        //set finalized
        rental.setFinalized(true);

        //set rentalEnd
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date end = formatter.parse(formObject.get("rentalEnd").getAsString());
        rental.setRentalEnd(end);

        //set comment
        rental.setComment(formObject.get("comment").getAsString());

        //set injuredShip and set injured Ship activity to false
        Optional<Ship> injuredShipOptional = shipRepositoryService.getShipById(formObject.get("injuredShip").getAsLong());
        Ship injuredShip = injuredShipOptional.isPresent() ? injuredShipOptional.get() : new Ship();
        injuredShip.setActive(false);
        rental.setInjuredShip(injuredShip);

        //set injuredOars and set injured oars activity to false
        JsonArray injuredOarsJsonArray = formObject.get("injuredOars").getAsJsonArray();
        List<Oar> injuredOars = new ArrayList<>();
        for (JsonElement oarIdJson : injuredOarsJsonArray) {
            Long oarId = oarIdJson.getAsLong();
            LOG.info("oar: {}", oarId);
            Optional<Oar> injuredOarOptional = oarRepositoryService.getOarById(oarId);
            Oar injuredOar = injuredOarOptional.isPresent() ? injuredOarOptional.get() : new Oar();
            injuredOar.setActive(false);
            injuredOars.add(injuredOar); //TODO: this shit doesn't seem to work for the last element...
        }
        rental.setInjuredOars(injuredOars);

        return returnData;
    }

}
