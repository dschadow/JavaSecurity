/*
 * Copyright (C) 2014 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.sessionhandling.users;

import de.dominikschadow.javasecurity.sessionhandling.greetings.GreetingService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 * Greeting ManagedBean which integrates a Spring Bean to return the user/ admin greeting to the calling JSF page.
 *
 * @author Dominik Schadow
 */
@ManagedBean
@RequestScoped
public class GreetingBean {
    @ManagedProperty(value = "#{greetingServiceImpl}")
    private GreetingService greetingService;

    public String greetUser() {
        return greetingService.greetUser();
    }

    public String greetAdmin() {
        return greetingService.greetAdmin();
    }

    public void setGreetingService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }
}
