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

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Deserializer class.
 *
 * @author Dominik Schadow
 */
class DeserializerTest {
    private static final String TEST_FILE = "test-deserialize-me.bin";

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void deserialize_validFile_returnsCorrectObject() throws Exception {
        SerializeMe original = new SerializeMe();
        original.setFirstname("Arthur");
        original.setLastname("Dent");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            oos.writeObject(original);
            oos.flush();
        }

        try (ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(TEST_FILE)))) {
            SerializeMe deserialized = (SerializeMe) is.readObject();

            assertNotNull(deserialized);
            assertEquals("Arthur", deserialized.getFirstname());
            assertEquals("Dent", deserialized.getLastname());
        }
    }

    @Test
    void deserialize_withNullValues_returnsObjectWithNullFields() throws Exception {
        SerializeMe original = new SerializeMe();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            oos.writeObject(original);
            oos.flush();
        }

        try (ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(TEST_FILE)))) {
            SerializeMe deserialized = (SerializeMe) is.readObject();

            assertNotNull(deserialized);
            assertNull(deserialized.getFirstname());
            assertNull(deserialized.getLastname());
        }
    }

    @Test
    void deserialize_nonExistentFile_throwsException() {
        assertThrows(Exception.class, () -> {
            try (ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream("non-existent-file.bin")))) {
                is.readObject();
            }
        });
    }

    @Test
    void deserialize_multipleObjects_returnsAllCorrectly() throws Exception {
        SerializeMe first = new SerializeMe();
        first.setFirstname("Ford");
        first.setLastname("Prefect");

        SerializeMe second = new SerializeMe();
        second.setFirstname("Zaphod");
        second.setLastname("Beeblebrox");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            oos.writeObject(first);
            oos.writeObject(second);
            oos.flush();
        }

        try (ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(TEST_FILE)))) {
            SerializeMe deserializedFirst = (SerializeMe) is.readObject();
            SerializeMe deserializedSecond = (SerializeMe) is.readObject();

            assertEquals("Ford", deserializedFirst.getFirstname());
            assertEquals("Prefect", deserializedFirst.getLastname());
            assertEquals("Zaphod", deserializedSecond.getFirstname());
            assertEquals("Beeblebrox", deserializedSecond.getLastname());
        }
    }
}
