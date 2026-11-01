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

import javax.crypto.DecapsulateException;
import javax.crypto.KEM;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.NamedParameterSpec;

/**
 * Post-quantum key encapsulation sample with plain Java using the Module-Lattice-Based Key-Encapsulation Mechanism
 * (ML-KEM, FIPS 203, JEP 496). Generates a fresh ML-KEM-768 key pair. The sender encapsulates a new shared secret with
 * the public key of the receiver, the receiver decapsulates the same shared secret with its private key.
 * <p/>
 * ML-KEM does not encrypt data itself. The shared secret is used as symmetric key (e.g. for AES-GCM) or as input for a
 * key derivation function like HKDF.
 * <p/>
 * A manipulated encapsulation does not throw an exception: ML-KEM uses implicit rejection and returns a different,
 * pseudo-random secret instead. Any later decryption with that secret fails.
 *
 * @author Dominik Schadow
 */
public class MLKEM {
    private static final String ALGORITHM = "ML-KEM";
    private static final String SECRET_KEY_ALGORITHM = "AES";

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(NamedParameterSpec.ML_KEM_768);
        return generator.generateKeyPair();
    }

    /**
     * Sender side: creates a new shared secret and its encapsulation for the given public key.
     *
     * @param publicKey The public key of the receiver
     * @return The shared secret (32 bytes, usable as AES-256 key) and the encapsulation to send to the receiver
     */
    public KEM.Encapsulated encapsulate(PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException {
        KEM kem = KEM.getInstance(ALGORITHM);
        KEM.Encapsulator encapsulator = kem.newEncapsulator(publicKey);
        return encapsulator.encapsulate(0, encapsulator.secretSize(), SECRET_KEY_ALGORITHM);
    }

    /**
     * Receiver side: recovers the shared secret from the received encapsulation.
     *
     * @param privateKey    The private key of the receiver
     * @param encapsulation The encapsulation received from the sender
     * @return The shared secret
     */
    public SecretKey decapsulate(PrivateKey privateKey, byte[] encapsulation) throws NoSuchAlgorithmException,
            InvalidKeyException, DecapsulateException {
        KEM kem = KEM.getInstance(ALGORITHM);
        KEM.Decapsulator decapsulator = kem.newDecapsulator(privateKey);
        return decapsulator.decapsulate(encapsulation, 0, decapsulator.secretSize(), SECRET_KEY_ALGORITHM);
    }
}
