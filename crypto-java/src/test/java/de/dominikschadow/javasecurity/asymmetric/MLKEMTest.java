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
package de.dominikschadow.javasecurity.asymmetric;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.KEM;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MLKEMTest {
    private final MLKEM mlkem = new MLKEM();
    private KeyPair receiverKeyPair;

    @BeforeEach
    protected void setup() throws Exception {
        receiverKeyPair = mlkem.generateKeyPair();
    }

    @Test
    void givenCorrectEncapsulationWhenDecapsulatingThenReturnSameSecret() throws Exception {
        KEM.Encapsulated encapsulated = mlkem.encapsulate(receiverKeyPair.getPublic());
        SecretKey senderSecret = encapsulated.key();

        SecretKey receiverSecret = mlkem.decapsulate(receiverKeyPair.getPrivate(), encapsulated.encapsulation());

        Assertions.assertAll(
                () -> assertEquals("AES", senderSecret.getAlgorithm()),
                () -> assertEquals(32, senderSecret.getEncoded().length),
                () -> assertArrayEquals(senderSecret.getEncoded(), receiverSecret.getEncoded())
        );
    }

    @Test
    void givenManipulatedEncapsulationWhenDecapsulatingThenReturnDifferentSecret() throws Exception {
        KEM.Encapsulated encapsulated = mlkem.encapsulate(receiverKeyPair.getPublic());
        byte[] manipulated = encapsulated.encapsulation().clone();
        manipulated[0] ^= 1;

        SecretKey receiverSecret = mlkem.decapsulate(receiverKeyPair.getPrivate(), manipulated);

        assertFalse(Arrays.equals(encapsulated.key().getEncoded(), receiverSecret.getEncoded()));
    }

    @Test
    void givenSharedSecretWhenEncryptingWithAesGcmThenReceiverCanDecrypt() throws Exception {
        final String initialText = "ML-KEM sample text";

        KEM.Encapsulated encapsulated = mlkem.encapsulate(receiverKeyPair.getPublic());
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        Cipher encrypt = Cipher.getInstance("AES/GCM/NoPadding");
        encrypt.init(Cipher.ENCRYPT_MODE, encapsulated.key(), new GCMParameterSpec(128, iv));
        byte[] ciphertext = encrypt.doFinal(initialText.getBytes(StandardCharsets.UTF_8));

        SecretKey receiverSecret = mlkem.decapsulate(receiverKeyPair.getPrivate(), encapsulated.encapsulation());
        Cipher decrypt = Cipher.getInstance("AES/GCM/NoPadding");
        decrypt.init(Cipher.DECRYPT_MODE, receiverSecret, new GCMParameterSpec(128, iv));
        byte[] plaintext = decrypt.doFinal(ciphertext);

        assertEquals(initialText, new String(plaintext, StandardCharsets.UTF_8));
    }

    @Test
    void givenOtherReceiverWhenDecryptingThenThrowException() throws Exception {
        KEM.Encapsulated encapsulated = mlkem.encapsulate(receiverKeyPair.getPublic());
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        Cipher encrypt = Cipher.getInstance("AES/GCM/NoPadding");
        encrypt.init(Cipher.ENCRYPT_MODE, encapsulated.key(), new GCMParameterSpec(128, iv));
        byte[] ciphertext = encrypt.doFinal("ML-KEM sample text".getBytes(StandardCharsets.UTF_8));

        SecretKey otherSecret = mlkem.decapsulate(mlkem.generateKeyPair().getPrivate(), encapsulated.encapsulation());
        Cipher decrypt = Cipher.getInstance("AES/GCM/NoPadding");
        decrypt.init(Cipher.DECRYPT_MODE, otherSecret, new GCMParameterSpec(128, iv));

        assertThrows(AEADBadTagException.class, () -> decrypt.doFinal(ciphertext));
    }
}
