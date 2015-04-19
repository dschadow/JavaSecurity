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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Dominik Schadow
 */
@Controller
@RequestMapping(value = "/user/list")
public class ListController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(method = GET)
    public String index(Model model) {
        List<Contact> contacts = jdbcTemplate.query("SELECT * FROM contacts WHERE username = ?", new
                        Object[]{getCurrentUsername()},
                (rs, rowNum) -> {
                    Contact c = new Contact();
                    c.setContactId(rs.getInt("contact_id"));
                    c.setUsername(rs.getString("username"));
                    c.setFirstname(rs.getString("firstname"));
                    c.setLastname(rs.getString("lastname"));
                    c.setComment(rs.getString("comment"));
                    return c;
                });

        logger.info("Found {} contacts for user {}", contacts.size(), getCurrentUsername());

        model.addAttribute("contacts", contacts);

        return "user/list";
    }

    private String getCurrentUsername() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
