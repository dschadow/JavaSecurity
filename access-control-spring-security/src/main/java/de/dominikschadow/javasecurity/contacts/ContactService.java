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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Service to load a contact identified by its id or to load all contacts for the authenticated user.
 *
 * @author Dominik Schadow
 */
@Service
@RequiredArgsConstructor
public class ContactService {
    private final JdbcTemplate jdbcTemplate;

    @PreAuthorize("hasRole('USER')")
    @PostAuthorize("returnObject.username == principal.username")
    Contact getContact(int contactId) {
        return jdbcTemplate.queryForObject("SELECT * FROM contacts WHERE id = ?",
                (rs, rowNum) -> createContact(rs), contactId);
    }

    /**
     * This method loads all contacts from the database and removes those contacts from the resulting list that don't
     * belong to the currently authenticated user. In a real application the select query would already contain the
     * user id and return only those contacts that the user is allowed to see. However, to demonstrate some Spring
     * Security capabilities, all filtering is done via the {@code PostFilter} annotation.
     *
     * @return The list of contacts for the currently authenticated user
     */
    @PreAuthorize("hasRole('USER')")
    @PostFilter("filterObject.username == principal.username")
    List<Contact> getContacts() {
        return jdbcTemplate.query("SELECT * FROM contacts", (rs, rowNum) -> createContact(rs));
    }

    private Contact createContact(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getLong("id"));
        contact.setUsername(rs.getString("username"));
        contact.setFirstname(rs.getString("firstname"));
        contact.setLastname(rs.getString("lastname"));
        contact.setComment(rs.getString("comment"));
        return contact;
    }
}
