package de.dominikschadow.javasecurity.tink.mac;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HmacShaWithGeneratedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);

    private HmacShaWithGeneratedKey hmac;

    @BeforeEach
    protected void setup() throws Exception {
        hmac = new HmacShaWithGeneratedKey();
    }

    @Test
    void unchangedInputValidatesSuccessful() throws Exception {
        KeysetHandle keysetHandle = hmac.generateKey();

        byte[] initialMac = hmac.computeMac(keysetHandle, INITIAL_TEXT);
        boolean validation = hmac.verifyMac(keysetHandle, initialMac, INITIAL_TEXT);

        Assertions.assertAll(
                () -> assertNotNull(initialMac),
                () -> assertTrue(validation)
        );
    }

    @Test
    void changedInputValidationFails() throws Exception {
        KeysetHandle keysetHandle = hmac.generateKey();

        byte[] initialMac = hmac.computeMac(keysetHandle, INITIAL_TEXT);
        boolean validation = hmac.verifyMac(keysetHandle, initialMac, "manipulation".getBytes(StandardCharsets.UTF_8));

        Assertions.assertAll(
                () -> assertNotNull(initialMac),
                () -> assertFalse(validation)
        );
    }
}