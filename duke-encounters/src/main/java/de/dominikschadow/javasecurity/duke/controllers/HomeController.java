package de.dominikschadow.javasecurity.duke.controllers;

import de.dominikschadow.javasecurity.duke.services.EncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HomeController {
    private EncounterService encounterService;

    @Autowired
    public HomeController(EncounterService encounterService) {
        this.encounterService = encounterService;
    }

    @RequestMapping(value = "/", method= GET)
    public String home(Model model) {

        return "index";
    }
}
