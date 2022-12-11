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
package de.dominikschadow.javasecurity.sessionhandling.greetings;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Greeting controller to return the user/ admin greeting to the caller.
 *
 * @author Dominik Schadow
 */
@Controller
@RequiredArgsConstructor
public class GreetingController {
    private final GreetingService greetingService;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("sessionId", session.getId());

        return "index";
    }

    @GetMapping("user/user")
    public String greetUser(Model model, HttpSession session) {
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("greeting", greetingService.greetUser());

        return "user/user";
    }

    @GetMapping("admin/admin")
    public String greetAdmin(Model model, HttpSession session) {
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("greeting", greetingService.greetAdmin());

        return "admin/admin";
    }
}
