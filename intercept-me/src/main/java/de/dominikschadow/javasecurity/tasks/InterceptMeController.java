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
package de.dominikschadow.javasecurity.tasks;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.util.StringUtils;

/**
 * Controller processing the main page and all forms. Returns <i>SUCCESS</i> or <i>FAILURE</i> depending on the given
 * input.
 *
 * @author Dominik Schadow
 */
@Controller
public class InterceptMeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("firstTask", new FirstTask(""));
        
        return "index";
    }

    @PostMapping("first")
    public String firstTask(FirstTask firstTask, Model model) {
        String result = "FAILURE";

        if (StringUtils.equals(firstTask.name(), "inject")) {
            result = "SUCCESS";
        }

        model.addAttribute("result", result);

        return "result";
    }

    @PostMapping("second")
    public String secondTask(Model model) {
        model.addAttribute("result", "FAILURE");

        return "result";
    }
}
