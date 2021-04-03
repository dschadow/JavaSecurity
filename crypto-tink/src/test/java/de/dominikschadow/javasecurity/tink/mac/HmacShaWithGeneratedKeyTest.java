package de.dominikschadow.javasecurity.tink.mac;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HmacShaWithGeneratedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);

    private final HmacShaWithGeneratedKey hmac = new HmacShaWithGeneratedKey();

    @Test
    void unchangedInputValidatesSuccessful() throws Exception {
        KeysetHandle keysetHandle = hmac.generateKey();

        byte[] tag = hmac.computeMac(keysetHandle, INITIAL_TEXT);
        boolean validation = hmac.verifyMac(keysetHandle, tag, INITIAL_TEXT);

        Assertions.assertAll(
                () -> assertNotNull(tag),
                () -> assertTrue(validation)
        );
    }

    @Test
    void changedInputValidationFails() throws Exception {
        KeysetHandle keysetHandle = hmac.generateKey();

        byte[] tag = hmac.computeMac(keysetHandle, INITIAL_TEXT);
        boolean validation = hmac.verifyMac(keysetHandle, tag, "manipulation".getBytes(StandardCharsets.UTF_8));

        Assertions.assertAll(
                () -> assertNotNull(tag),
                () -> assertFalse(validation)
        );
    }
}