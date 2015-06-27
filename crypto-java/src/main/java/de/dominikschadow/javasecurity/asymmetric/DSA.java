/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
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

import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Digital signature sample with plain Java. Loads the DSA key from the sample keystore, signs and verifies sample
 * text with it.
 * <p/>
 * Uses Google Guava to Base64 print the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class DSA {
    private static final Logger LOGGER = LoggerFactory.getLogger(DSA.class);
    private static final String ALGORITHM = "SHA1withDSA";
    private static final String KEYSTORE_PATH = "/samples.ks";

    public static void main(String[] args) {
        DSA aes = new DSA();
        final String initialText = "DSA signature sample text";
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "asymmetric-sample-dsa";
        final char[] keyPassword = "asymmetric-sample-dsa".toCharArray();

        try {
            KeyStore ks = aes.loadKeystore(KEYSTORE_PATH, keystorePassword);
            PrivateKey privateKey = aes.loadPrivateKey(ks, keyAlias, keyPassword);
            PublicKey publicKey = aes.loadPublicKey(ks, keyAlias);

            byte[] signature = aes.sign(privateKey, initialText);
            boolean valid = aes.verify(publicKey, signature, initialText);

            aes.printReadableMessages(initialText, signature, valid);
        } catch (NoSuchAlgorithmException | SignatureException | KeyStoreException | CertificateException |
                UnrecoverableKeyException | InvalidKeyException | IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private KeyStore loadKeystore(String keystorePath, char[] keystorePassword) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException {
        InputStream keystoreStream = getClass().getResourceAsStream(keystorePath);

        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(keystoreStream, keystorePassword);

        return ks;
    }

    private PrivateKey loadPrivateKey(KeyStore ks, String keyAlias, char[] keyPassword) throws KeyStoreException,
            UnrecoverableKeyException, NoSuchAlgorithmException {
        if (!ks.containsAlias(keyAlias)) {
            throw new UnrecoverableKeyException("Private key " + keyAlias + " not found in keystore");
        }

        return (PrivateKey) ks.getKey(keyAlias, keyPassword);
    }

    private PublicKey loadPublicKey(KeyStore ks, String keyAlias) throws KeyStoreException, UnrecoverableKeyException {
        if (!ks.containsAlias(keyAlias)) {
            throw new UnrecoverableKeyException("Public key " + keyAlias + " not found in keystore");
        }

        return ks.getCertificate(keyAlias).getPublicKey();
    }

    private byte[] sign(PrivateKey privateKey, String initialText) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature dsa = Signature.getInstance(ALGORITHM);
        dsa.initSign(privateKey);
        dsa.update(initialText.getBytes("UTF-8"));
        return dsa.sign();
    }

    private boolean verify(PublicKey publicKey, byte[] signature, String initialText) throws
            NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature dsa = Signature.getInstance(ALGORITHM);
        dsa.initVerify(publicKey);
        dsa.update(initialText.getBytes("UTF-8"));
        return dsa.verify(signature);
    }

    private void printReadableMessages(String initialText, byte[] signature, boolean valid) {
        LOGGER.info("initialText: {}", initialText);
        LOGGER.info("signature as byte[]: {}", new String(signature));
        LOGGER.info("signature as Base64: {}", BaseEncoding.base64().encode(signature));
        LOGGER.info("signature valid: {}", valid);
    }
}
