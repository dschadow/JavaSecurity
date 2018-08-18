/*
 * Copyright (C) 2018 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.home;

import org.owasp.esapi.errors.AccessControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.net.URLConnection;

/**
 * Index controller for all home page related operations.
 *
 * @author Dominik Schadow
 */
@Controller
@RequestMapping
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    private final ResourceService resourceService;

    public IndexController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("indirectReferences", resourceService.getAllIndirectReferences());

        return "index";
    }

    @GetMapping("download")
    @ResponseBody
    public ResponseEntity<Resource> download(@RequestParam("name") String name) {
        try {
            String originalName = resourceService.getFileByIndirectReference(name).getName();
            String contentType = URLConnection.guessContentTypeFromName(originalName);
            Resource resource = resourceService.loadAsResource(originalName);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
        } catch (MalformedURLException | AccessControlException ex) {
            log.error(ex.getMessage(), ex);
        }

        return ResponseEntity.notFound().build();
    }
}
