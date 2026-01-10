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
package de.dominikschadow.javasecurity.serialize;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Serializer class.
 *
 * @author Dominik Schadow
 */
class SerializerTest {
    private static final String TEST_FILE = "test-serialize-me.bin";

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void serializeMe_canBeSerializedAndDeserialized() throws Exception {
        SerializeMe serializeMe = new SerializeMe();
        serializeMe.setFirstname("Arthur");
        serializeMe.setLastname("Dent");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            oos.writeObject(serializeMe);
            oos.flush();
        }

        File file = new File(TEST_FILE);
        assertTrue(file.exists(), "Serialized file should exist");

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEST_FILE))) {
            SerializeMe deserialized = (SerializeMe) ois.readObject();
            assertEquals("Arthur", deserialized.getFirstname());
            assertEquals("Dent", deserialized.getLastname());
        }
    }

    @Test
    void serializeMe_createsFile() throws Exception {
        SerializeMe serializeMe = new SerializeMe();
        serializeMe.setFirstname("Ford");
        serializeMe.setLastname("Prefect");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            oos.writeObject(serializeMe);
            oos.flush();
        }

        File file = new File(TEST_FILE);
        assertTrue(file.exists(), "Serialized file should be created");
        assertTrue(file.length() > 0, "Serialized file should not be empty");
    }

    @Test
    void serializeMe_withNullValues_canBeSerialized() throws Exception {
        SerializeMe serializeMe = new SerializeMe();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            oos.writeObject(serializeMe);
            oos.flush();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEST_FILE))) {
            SerializeMe deserialized = (SerializeMe) ois.readObject();
            assertNull(deserialized.getFirstname());
            assertNull(deserialized.getLastname());
        }
    }
}
