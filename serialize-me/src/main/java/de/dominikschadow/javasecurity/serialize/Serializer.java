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
package de.dominikschadow.javasecurity.serialize;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Serializer {
    public static void main(String[] args) {
        SerializeMe serializeMe = new SerializeMe();
        serializeMe.setFirstname("Arthur");
        serializeMe.setLastname("Dent");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serialize-me.bin"))) {
            oos.writeObject(serializeMe);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
