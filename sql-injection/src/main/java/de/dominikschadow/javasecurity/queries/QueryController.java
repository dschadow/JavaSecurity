/*
 * Copyright (C) 2021 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.queries;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Reacts to all query requests of the sample application.
 *
 * @author Dominik Schadow
 */
@Controller
public class QueryController {
    private final PlainSqlQuery plainSqlQuery;
    private final EscapedQuery escapedQuery;
    private final PreparedStatementQuery preparedStatementQuery;

    public QueryController(PlainSqlQuery plainSqlQuery, EscapedQuery escapedQuery, PreparedStatementQuery preparedStatementQuery) {
        this.plainSqlQuery = plainSqlQuery;
        this.escapedQuery = escapedQuery;
        this.preparedStatementQuery = preparedStatementQuery;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("plain", new Customer());
        model.addAttribute("escaped", new Customer());
        model.addAttribute("prepared", new Customer());
        model.addAttribute("hql", new Customer());

        return "index";
    }

    /**
     * Handles requests for a plain SQL query.
     *
     * @param customer The Customer data
     * @param model    The model
     * @return The result page
     */
    @PostMapping("plain")
    public String plainQuery(@ModelAttribute Customer customer, Model model) {
        model.addAttribute("customers", plainSqlQuery.query(customer.getName()));

        return "result";
    }

    /**
     * Handles requests for an escaped SQL query.
     *
     * @param customer The Customer data
     * @param model    The model
     * @return The result page
     */
    @PostMapping("escaped")
    public String escapedQuery(@ModelAttribute Customer customer, Model model) {
        model.addAttribute("customers", escapedQuery.query(customer.getName()));

        return "result";
    }

    /**
     * Handles requests for a prepared statement SQL query.
     *
     * @param customer The Customer data
     * @param model    The model
     * @return The result page
     */
    @PostMapping("prepared")
    public String preparedQuery(@ModelAttribute Customer customer, Model model) {
        model.addAttribute("customers", preparedStatementQuery.query(customer.getName()));

        return "result";
    }
}
