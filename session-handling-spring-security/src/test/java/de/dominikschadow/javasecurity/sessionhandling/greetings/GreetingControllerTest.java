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
package de.dominikschadow.javasecurity.sessionhandling.greetings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GreetingController.class)
@AutoConfigureMockMvc(addFilters = false)
class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GreetingService greetingService;

    @Test
    void index_shouldReturnIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("sessionId"));
    }

    @Test
    void greetUser_shouldReturnUserViewWithGreeting() throws Exception {
        when(greetingService.greetUser()).thenReturn("Hello User!");

        mockMvc.perform(get("/user/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user"))
                .andExpect(model().attributeExists("sessionId"))
                .andExpect(model().attribute("greeting", "Hello User!"));
    }

    @Test
    void greetAdmin_shouldReturnAdminViewWithGreeting() throws Exception {
        when(greetingService.greetAdmin()).thenReturn("Hello Admin!");

        mockMvc.perform(get("/admin/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin"))
                .andExpect(model().attributeExists("sessionId"))
                .andExpect(model().attribute("greeting", "Hello Admin!"));
    }
}
