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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests the access control with the complete Spring Security filter chain and the sample data: a user must not be
 * able to access contacts of another user by changing the contact id in the URL.
 *
 * @author Dominik Schadow
 */
@SpringBootTest
class ContactAccessIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    /**
     * MockMvc is created manually instead of using {@code @AutoConfigureMockMvc} to reuse the application context of
     * the other {@code @SpringBootTest} classes. A second context would try to create the embedded database again.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void index_withoutAuthentication_isAccessible() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void contacts_withoutAuthentication_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/contacts"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(username = "userA", roles = "USER")
    void contacts_asUserA_showsOnlyOwnContacts() throws Exception {
        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("contacts", hasSize(2)))
                .andExpect(model().attribute("contacts", everyItem(hasProperty("username", is("userA")))));
    }

    @Test
    @WithMockUser(username = "userA", roles = "USER")
    void contactDetails_asUserA_withOwnContact_isAccessible() throws Exception {
        mockMvc.perform(get("/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts/details"))
                .andExpect(model().attribute("contact", hasProperty("username", is("userA"))));
    }

    @Test
    @WithMockUser(username = "userA", roles = "USER")
    void contactDetails_asUserA_withContactOfUserB_isForbidden() throws Exception {
        // contact with id 3 belongs to userB
        mockMvc.perform(get("/contacts/3"))
                .andExpect(status().isForbidden());
    }
}
