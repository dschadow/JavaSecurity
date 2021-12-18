/*
 * Copyright (C) 2021 Dominik Schadow, dominikschadow@gmail.com
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Digital signature sample with plain Java. Loads the DSA key from the sample keystore, signs and verifies sample text
 * with it.
 * <p/>
 * Uses Google Guava to hex the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class DSA {
    private static final System.Logger LOG = System.getLogger(DSA.class.getName());
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
            LOG.log(System.Logger.Level.ERROR, ex.getMessage(), ex);
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
            InvalidKeyException, SignatureException {
        Signature dsa = Signature.getInstance(ALGORITHM);
        dsa.initSign(privateKey);
        dsa.update(initialText.getBytes(StandardCharsets.UTF_8));
        return dsa.sign();
    }

    private static boolean verify(PublicKey publicKey, byte[] signature, String initialText) throws
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature dsa = Signature.getInstance(ALGORITHM);
        dsa.initVerify(publicKey);
        dsa.update(initialText.getBytes(StandardCharsets.UTF_8));
        return dsa.verify(signature);
    }

    private static void printReadableMessages(String initialText, byte[] signature, boolean valid) {
        LOG.log(System.Logger.Level.INFO, "initial text: {0}", initialText);
        LOG.log(System.Logger.Level.INFO, "signature: {0}", BaseEncoding.base16().encode(signature));
        LOG.log(System.Logger.Level.INFO, "signature valid: {0}", valid);
    }
}
