/*
 * Copyright (C) 2020 Dominik Schadow, dominikschadow@gmail.com
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Converts the database result rows into a List of Customers.
 *
 * @author Dominik Schadow
 */
class CustomerRowMapper {
    static List<Customer> mapRows(List<Map<String, Object>> rows) {
        List<Customer> customers = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Customer customer = new Customer();
            customer.setId((Integer) row.get("id"));
            customer.setName((String) row.get("name"));
            customer.setStatus((String) row.get("status"));
            customer.setOrderLimit((Integer) row.get("order_limit"));

            customers.add(customer);
        }

        return customers;
    }
}
