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
package de.dominikschadow.javasecurity.sessionhandling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests the URL based access rules of the {@link SecurityConfig} with the complete filter chain.
 *
 * @author Dominik Schadow
 */
@SpringBootTest
class SecurityConfigTest {
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
    void userPage_withoutAuthentication_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/user/user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void adminPage_withoutAuthentication_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/admin/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userPage_withUserRole_isAccessible() throws Exception {
        mockMvc.perform(get("/user/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void adminPage_withUserRole_isForbidden() throws Exception {
        mockMvc.perform(get("/admin/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void userPage_withAdminRole_isAccessible() throws Exception {
        mockMvc.perform(get("/user/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPage_withAdminRole_isAccessible() throws Exception {
        mockMvc.perform(get("/admin/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin"));
    }
}
