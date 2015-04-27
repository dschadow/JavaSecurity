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
package de.dominikschadow.javasecurity.controller;

import de.dominikschadow.javasecurity.domain.Contact;
import de.dominikschadow.javasecurity.services.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Dominik Schadow
 */
@Controller
@RequestMapping(value = "/contacts/{contactId}")
public class DetailsController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ContactService contactService;

    @RequestMapping(method = GET)
    public String details(@PathVariable int contactId, Model model) {
        logger.info("Loading contact with ID {} for user", contactId);

        Contact contact = contactService.getContact(contactId);

        model.addAttribute("contact", contact);

        return "contacts/details";
    }
}
