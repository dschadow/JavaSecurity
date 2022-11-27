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
package de.dominikschadow.javasecurity.logging.home;

import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Simple login controller which returns a success message and logs security relevant events into the log file.
 *
 * @author Dominik Schadow
 */
@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("login", new Login("", ""));

        return "index";
    }

    @PostMapping("login")
    public String firstTask(Login login, Model model) {
        String username = login.username();
        String password = login.password();

        log.info(SecurityMarkers.CONFIDENTIAL, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.EVENT_FAILURE, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.EVENT_SUCCESS, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.RESTRICTED, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.SECRET, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.SECURITY_AUDIT, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.SECURITY_FAILURE, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.SECURITY_SUCCESS, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.TOP_SECRET, "User {} with password {} logged in", username, password);

        model.addAttribute("login", login);

        return "login";
    }
}
