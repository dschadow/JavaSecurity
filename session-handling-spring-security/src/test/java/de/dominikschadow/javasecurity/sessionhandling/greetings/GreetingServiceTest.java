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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    @WithMockUser(roles = "USER")
    void greetUser_withUserRole_shouldReturnGreeting() {
        String greeting = greetingService.greetUser();

        assertEquals("Spring Security says hello to the user!", greeting);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void greetUser_withAdminRole_shouldReturnGreeting() {
        String greeting = greetingService.greetUser();

        assertEquals("Spring Security says hello to the user!", greeting);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void greetAdmin_withAdminRole_shouldReturnGreeting() {
        String greeting = greetingService.greetAdmin();

        assertEquals("Spring Security says hello to the admin!", greeting);
    }

    @Test
    @WithMockUser(roles = "USER")
    void greetAdmin_withUserRole_shouldThrowAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () -> greetingService.greetAdmin());
    }

    @Test
    void greetUser_withoutAuthentication_shouldThrowAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> greetingService.greetUser());
    }

    @Test
    void greetAdmin_withoutAuthentication_shouldThrowAuthenticationCredentialsNotFoundException() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> greetingService.greetAdmin());
    }
}
