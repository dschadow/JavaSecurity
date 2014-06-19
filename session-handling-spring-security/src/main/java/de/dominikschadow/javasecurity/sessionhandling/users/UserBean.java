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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Simple ManagedBean to log out the current user and to return the current session id.
 *
 * @author Dominik Schadow
 */
@ManagedBean(name = "userBean")
@RequestScoped
public class UserBean {
    public String logout() {
        SecurityContextHolder.clearContext();
        return "/logout";
    }

    public String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}
