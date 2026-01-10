/*
 * Copyright (C) 2026 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dominikschadow.javasecurity.contacts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ContactService} to verify Spring Security method-level security annotations.
 *
 * @author Dominik Schadow
 */
@SpringBootTest
class ContactServiceTest {
    @Autowired
    private ContactService contactService;

    @Test
    void getContact_withoutAuthentication_throwsException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> contactService.getContact(1));
    }

    @Test
    @WithMockUser(username = "userA", roles = "USER")
    void getContact_asUserA_withOwnContact_returnsContact() {
        Contact contact = contactService.getContact(1);

        assertNotNull(contact);
        assertEquals("userA", contact.getUsername());
        assertEquals("Zaphod", contact.getFirstname());
        assertEquals("Beeblebrox", contact.getLastname());
    }

    @Test
    @WithMockUser(username = "userA", roles = "USER")
    void getContact_asUserA_withOtherUsersContact_throwsAccessDenied() {
        // Contact with id 3 belongs to userB
        assertThrows(AccessDeniedException.class, () -> contactService.getContact(3));
    }

    @Test
    @WithMockUser(username = "userB", roles = "USER")
    void getContact_asUserB_withOwnContact_returnsContact() {
        Contact contact = contactService.getContact(3);

        assertNotNull(contact);
        assertEquals("userB", contact.getUsername());
        assertEquals("Arthur", contact.getFirstname());
        assertEquals("Dent", contact.getLastname());
    }

    @Test
    @WithMockUser(username = "userB", roles = "USER")
    void getContact_asUserB_withOtherUsersContact_throwsAccessDenied() {
        // Contact with id 1 belongs to userA
        assertThrows(AccessDeniedException.class, () -> contactService.getContact(1));
    }

    @Test
    @WithMockUser(username = "userA", roles = "ADMIN")
    void getContact_withWrongRole_throwsAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> contactService.getContact(1));
    }

    @Test
    void getContacts_withoutAuthentication_throwsException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> contactService.getContacts());
    }

    @Test
    @WithMockUser(username = "userA", roles = "USER")
    void getContacts_asUserA_returnsOnlyUserAContacts() {
        List<Contact> contacts = contactService.getContacts();

        assertNotNull(contacts);
        assertEquals(2, contacts.size());
        assertTrue(contacts.stream().allMatch(c -> "userA".equals(c.getUsername())));
        assertTrue(contacts.stream().anyMatch(c -> "Zaphod".equals(c.getFirstname())));
        assertTrue(contacts.stream().anyMatch(c -> "Ford".equals(c.getFirstname())));
    }

    @Test
    @WithMockUser(username = "userB", roles = "USER")
    void getContacts_asUserB_returnsOnlyUserBContacts() {
        List<Contact> contacts = contactService.getContacts();

        assertNotNull(contacts);
        assertEquals(2, contacts.size());
        assertTrue(contacts.stream().allMatch(c -> "userB".equals(c.getUsername())));
        assertTrue(contacts.stream().anyMatch(c -> "Arthur".equals(c.getFirstname())));
        assertTrue(contacts.stream().anyMatch(c -> "Tricia Marie".equals(c.getFirstname())));
    }

    @Test
    @WithMockUser(username = "userC", roles = "USER")
    void getContacts_asUserWithNoContacts_returnsEmptyList() {
        List<Contact> contacts = contactService.getContacts();

        assertNotNull(contacts);
        assertTrue(contacts.isEmpty());
    }

    @Test
    @WithMockUser(username = "userA", roles = "ADMIN")
    void getContacts_withWrongRole_throwsAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> contactService.getContacts());
    }
}
