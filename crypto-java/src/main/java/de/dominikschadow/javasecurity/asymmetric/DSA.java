/*
 * Copyright (C) 2022 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.asymmetric;

import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * Digital signature sample with plain Java. Loads the DSA key from the sample keystore, signs and verifies sample text
 * with it.
 * <p/>
 * Uses Google Guava to hex the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class DSA {
    private static final String ALGORITHM = "SHA1withDSA";

    public byte[] sign(PrivateKey privateKey, String initialText) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        Signature dsa = Signature.getInstance(ALGORITHM);
        dsa.initSign(privateKey);
        dsa.update(initialText.getBytes(StandardCharsets.UTF_8));
        return dsa.sign();
    }

    public boolean verify(PublicKey publicKey, byte[] signature, String initialText) throws
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature dsa = Signature.getInstance(ALGORITHM);
        dsa.initVerify(publicKey);
        dsa.update(initialText.getBytes(StandardCharsets.UTF_8));
        return dsa.verify(signature);
    }
}
