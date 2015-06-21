/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dominikschadow.javasecurity.duke.controllers;

import de.dominikschadow.javasecurity.duke.services.EncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/encounters")
public class EncountersController {
    private EncounterService encounterService;

    @Autowired
    public EncountersController(EncounterService encounterService) {
        this.encounterService = encounterService;
    }

    @RequestMapping(method = GET)
    public String getAllEncounters(Model model) {

        return "encounters";
    }

    @RequestMapping(params = "type", method = GET)
    public String getEncountersByType(@RequestParam("type") String type, Model model) {
        return "encounters";
    }

    @RequestMapping(value = "/{id}", method = GET)
    public String getEncounter(@PathVariable("id") long id, Model model) {

        return "encounterDetails";
    }
}
