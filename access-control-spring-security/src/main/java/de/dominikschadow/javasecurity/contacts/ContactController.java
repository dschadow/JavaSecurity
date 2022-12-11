/*
 * Copyright (C) 2022 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.contacts;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Contact controller for all contact related operations.
 *
 * @author Dominik Schadow
 */
@Controller
@RequestMapping(value = "/contacts")
@RequiredArgsConstructor
public class ContactController {
    private static final Logger log = LoggerFactory.getLogger(ContactController.class);
    private final ContactService contactService;

    @GetMapping
    public String list(Model model) {
        List<Contact> contacts = contactService.getContacts();

        log.info("Found {} contacts for user", contacts.size());

        model.addAttribute("contacts", contacts);

        return "contacts/list";
    }

    @GetMapping("{contactId}")
    public String details(@PathVariable int contactId, Model model) {
        log.info("Loading contact with ID {} for user", contactId);

        Contact contact = contactService.getContact(contactId);

        model.addAttribute("contact", contact);

        return "contacts/details";
    }
}
