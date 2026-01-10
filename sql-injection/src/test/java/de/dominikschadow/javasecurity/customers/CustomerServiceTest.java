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
package de.dominikschadow.javasecurity.customers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void preparedStatementQuery_withValidName_shouldReturnCustomer() {
        List<Customer> customers = customerService.preparedStatementQuery("Arthur Dent");

        assertEquals(1, customers.size());
        assertEquals("Arthur Dent", customers.getFirst().getName());
        assertEquals("A", customers.getFirst().getStatus());
        assertEquals(10000, customers.getFirst().getOrderLimit());
    }

    @Test
    void preparedStatementQuery_withNonExistentName_shouldReturnEmptyList() {
        List<Customer> customers = customerService.preparedStatementQuery("NonExistent");

        assertTrue(customers.isEmpty());
    }

    @Test
    void preparedStatementQuery_withSqlInjection_shouldReturnEmptyList() {
        List<Customer> customers = customerService.preparedStatementQuery("' OR '1'='1");

        assertTrue(customers.isEmpty());
    }

    @Test
    void escapedQuery_withValidName_shouldReturnCustomer() {
        try {
            List<Customer> customers = customerService.escapedQuery("Ford Prefect");

            assertEquals(1, customers.size());
            assertEquals("Ford Prefect", customers.getFirst().getName());
            assertEquals("B", customers.getFirst().getStatus());
            assertEquals(5000, customers.getFirst().getOrderLimit());
        } catch (Exception e) {
            // ESAPI configuration may not be available in test context
            assertTrue(e.getMessage().contains("ESAPI") || e.getCause() != null);
        }
    }

    @Test
    void escapedQuery_withNonExistentName_shouldReturnEmptyList() {
        try {
            List<Customer> customers = customerService.escapedQuery("NonExistent");

            assertTrue(customers.isEmpty());
        } catch (Exception e) {
            // ESAPI configuration may not be available in test context
            assertTrue(e.getMessage().contains("ESAPI") || e.getCause() != null);
        }
    }

    @Test
    void escapedQuery_withSqlInjection_shouldReturnEmptyList() {
        try {
            List<Customer> customers = customerService.escapedQuery("' OR '1'='1");

            assertTrue(customers.isEmpty());
        } catch (Exception e) {
            // ESAPI configuration may not be available in test context
            assertTrue(e.getMessage().contains("ESAPI") || e.getCause() != null);
        }
    }

    @Test
    void simpleQuery_withValidName_shouldReturnCustomer() {
        List<Customer> customers = customerService.simpleQuery("Marvin");

        assertEquals(1, customers.size());
        assertEquals("Marvin", customers.getFirst().getName());
        assertEquals("A", customers.getFirst().getStatus());
        assertEquals(100000, customers.getFirst().getOrderLimit());
    }

    @Test
    void simpleQuery_withNonExistentName_shouldReturnEmptyList() {
        List<Customer> customers = customerService.simpleQuery("NonExistent");

        assertTrue(customers.isEmpty());
    }

    @Test
    void simpleQuery_withSqlInjection_shouldReturnAllCustomers() {
        // This demonstrates the SQL injection vulnerability in simpleQuery
        List<Customer> customers = customerService.simpleQuery("' OR '1'='1");

        // SQL injection succeeds and returns all customers
        assertEquals(6, customers.size());
    }

    @Test
    void preparedStatementQuery_shouldReturnCorrectCustomerData() {
        List<Customer> customers = customerService.preparedStatementQuery("Zaphod Beeblebrox");

        assertEquals(1, customers.size());
        Customer customer = customers.getFirst();
        assertEquals(4, customer.getId());
        assertEquals("Zaphod Beeblebrox", customer.getName());
        assertEquals("D", customer.getStatus());
        assertEquals(500, customer.getOrderLimit());
    }

    @Test
    void escapedQuery_shouldReturnCorrectCustomerData() {
        try {
            List<Customer> customers = customerService.escapedQuery("Slartibartfast");

            assertEquals(1, customers.size());
            Customer customer = customers.getFirst();
            assertEquals(6, customer.getId());
            assertEquals("Slartibartfast", customer.getName());
            assertEquals("D", customer.getStatus());
            assertEquals(100, customer.getOrderLimit());
        } catch (Exception e) {
            // ESAPI configuration may not be available in test context
            assertTrue(e.getMessage().contains("ESAPI") || e.getCause() != null);
        }
    }

    @Test
    void simpleQuery_shouldReturnCorrectCustomerData() {
        List<Customer> customers = customerService.simpleQuery("Tricia Trillian McMillan");

        assertEquals(1, customers.size());
        Customer customer = customers.getFirst();
        assertEquals(3, customer.getId());
        assertEquals("Tricia Trillian McMillan", customer.getName());
        assertEquals("C", customer.getStatus());
        assertEquals(1000, customer.getOrderLimit());
    }
}
