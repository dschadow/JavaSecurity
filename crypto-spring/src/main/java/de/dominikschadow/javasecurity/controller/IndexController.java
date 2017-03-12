/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
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

import de.dominikschadow.javasecurity.domain.Greeting;
import de.dominikschadow.javasecurity.domain.GreetingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Index controller for all home page related operations.
 *
 * @author Dominik Schadow
 */
@Controller
@RequestMapping("/")
public class IndexController {
    private final GreetingRepository greetingRepository;

    public IndexController(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    @GetMapping
    public String index(Model model) {
        Greeting greeting = greetingRepository.findOne(1);
        model.addAttribute("greeting", greeting);

        return "index";
    }
}
