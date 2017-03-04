/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
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
 * Uses Google Guava to hex the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class DSA {
    private static final Logger log = LoggerFactory.getLogger(DSA.class);
    private static final String ALGORITHM = "SHA1withDSA";
    private static final String KEYSTORE_PATH = "/samples.ks";

    /**
     * Private constructor.
     */
    private DSA() {
    }

    public static void main(String[] args) {
        sign();
    }

    private static void sign() {
        final String initialText = "DSA signature sample text";
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "asymmetric-sample-dsa";
        final char[] keyPassword = "asymmetric-sample-dsa".toCharArray();

        try {
            KeyStore ks = loadKeystore(keystorePassword);
            PrivateKey privateKey = loadPrivateKey(ks, keyAlias, keyPassword);
            PublicKey publicKey = loadPublicKey(ks, keyAlias);

            byte[] signature = sign(privateKey, initialText);
            boolean valid = verify(publicKey, signature, initialText);

            printReadableMessages(initialText, signature, valid);
        } catch (NoSuchAlgorithmException | SignatureException | KeyStoreException | CertificateException |
                UnrecoverableKeyException | InvalidKeyException | IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private static KeyStore loadKeystore(char[] keystorePassword) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException {
        try (InputStream keystoreStream = DSA.class.getResourceAsStream(KEYSTORE_PATH)) {
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(keystoreStream, keystorePassword);
            return ks;
        }
    }

    private static PrivateKey loadPrivateKey(KeyStore ks, String keyAlias, char[] keyPassword) throws KeyStoreException,
            UnrecoverableKeyException, NoSuchAlgorithmException {
        if (!ks.containsAlias(keyAlias)) {
            throw new UnrecoverableKeyException("Private key " + keyAlias + " not found in keystore");
        }

        return (PrivateKey) ks.getKey(keyAlias, keyPassword);
    }

    private static PublicKey loadPublicKey(KeyStore ks, String keyAlias) throws KeyStoreException, UnrecoverableKeyException {
        if (!ks.containsAlias(keyAlias)) {
            throw new UnrecoverableKeyException("Public key " + keyAlias + " not found in keystore");
        }

        return ks.getCertificate(keyAlias).getPublicKey();
    }

    private static byte[] sign(PrivateKey privateKey, String initialText) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature dsa = Signature.getInstance(ALGORITHM);
        dsa.initSign(privateKey);
        dsa.update(initialText.getBytes("UTF-8"));
        return dsa.sign();
    }

    private static boolean verify(PublicKey publicKey, byte[] signature, String initialText) throws
            NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature dsa = Signature.getInstance(ALGORITHM);
        dsa.initVerify(publicKey);
        dsa.update(initialText.getBytes("UTF-8"));
        return dsa.verify(signature);
    }

    private static void printReadableMessages(String initialText, byte[] signature, boolean valid) {
        log.info("initial text: {}", initialText);
        log.info("signature: {}", BaseEncoding.base16().encode(signature));
        log.info("signature valid: {}", valid);
    }
}
