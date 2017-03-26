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

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
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
        model.addAttribute("rental", match);
        model.addAttribute("end", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));
        model.addAttribute("submit", "return submitFinalRentalLog(" + id + ")");
        return "rental_log/rental_finalize";
    }

    @RequestMapping(value = "/rentals/comment/{rentalId}", method = RequestMethod.OPTIONS)
    public String rentalLogComment(@PathVariable("rentalId") Long id, Model model){
        Optional<RentalLog> rentalLog = rentalLogRepositoryService.getRentalLogById(id);
        model.addAttribute("rental", rentalLog.isPresent() ? rentalLog.get() : new RentalLog());
        model.addAttribute("submit", "return submitFinalRentalLog(" + id + ")");
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

    @RequestMapping(value = "/rentals/final/transaction")
    public String finalRentalTransaction(@RequestParam("rentalId") String rentalId,
                                       @RequestParam("shipId") String shipId,
                                       @RequestParam("oarId") String[] oarIds,
                                         HttpServletResponse response) {
        //TODO: implement rental finalize logic with injuries
         System.out.println(rentalId);
         System.out.println(shipId);
         for (String oarId : oarIds) {
             System.out.println(oarId);
         }
        System.out.println(response.getStatus());
         return "rentals";
     }

}
