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

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.NamedParameterSpec;

/**
 * Post-quantum digital signature sample with plain Java using the Module-Lattice-Based Digital Signature Algorithm
 * (ML-DSA, FIPS 204, JEP 497). Generates a fresh ML-DSA-65 key pair, signs and verifies sample text with it.
 * <p/>
 * ML-DSA is the quantum-resistant successor for classic signature algorithms like DSA, RSA or ECDSA. Signatures and
 * keys are considerably larger than with classic algorithms.
 *
 * @author Dominik Schadow
 */
public class MLDSA {
    private static final String ALGORITHM = "ML-DSA";

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(NamedParameterSpec.ML_DSA_65);
        return generator.generateKeyPair();
    }

    public byte[] sign(PrivateKey privateKey, String initialText) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        Signature mldsa = Signature.getInstance(ALGORITHM);
        mldsa.initSign(privateKey);
        mldsa.update(initialText.getBytes(StandardCharsets.UTF_8));
        return mldsa.sign();
    }

    public boolean verify(PublicKey publicKey, byte[] signature, String initialText) throws
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature mldsa = Signature.getInstance(ALGORITHM);
        mldsa.initVerify(publicKey);
        mldsa.update(initialText.getBytes(StandardCharsets.UTF_8));
        return mldsa.verify(signature);
    }
}
