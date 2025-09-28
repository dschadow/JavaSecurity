/*
 * Copyright (C) 2025 Dominik Schadow, dominikschadow@gmail.com
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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ContactController.class)
class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    private Contact sampleContact(long id, String username, String firstname, String lastname) {
        Contact c = new Contact();
        c.setId(id);
        c.setUsername(username);
        c.setFirstname(firstname);
        c.setLastname(lastname);
        c.setComment("test");
        return c;
    }

    @Test
    @WithMockUser(username = "userA")
    void listContacts_asUser_ok() throws Exception {
        List<Contact> contacts = List.of(
                sampleContact(1L, "userA", "Alice", "Anderson"),
                sampleContact(2L, "userA", "Alan", "Archer")
        );
        Mockito.when(contactService.getContacts()).thenReturn(contacts);

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts/list"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attribute("contacts", hasSize(2)))
                .andExpect(model().attribute("contacts", hasItem(allOf(
                        hasProperty("id", is(1L)),
                        hasProperty("username", is("userA")),
                        hasProperty("firstname", is("Alice")),
                        hasProperty("lastname", is("Anderson"))
                ))));
    }

    @Test
    @WithMockUser(username = "userA")
    void contactDetails_asUser_ok() throws Exception {
        Contact contact = sampleContact(42L, "userA", "Bob", "Baker");
        Mockito.when(contactService.getContact(42)).thenReturn(contact);

        mockMvc.perform(get("/contacts/42"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts/details"))
                .andExpect(model().attributeExists("contact"))
                .andExpect(model().attribute("contact", allOf(
                        hasProperty("id", is(42L)),
                        hasProperty("username", is("userA")),
                        hasProperty("firstname", is("Bob")),
                        hasProperty("lastname", is("Baker"))
                )));
    }

    @Test
    void listContacts_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/contacts"))
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(containsString("Unauthorized")));
    }

    @Test
    void contactDetails_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/contacts/42"))
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(containsString("Unauthorized")));
    }
}
