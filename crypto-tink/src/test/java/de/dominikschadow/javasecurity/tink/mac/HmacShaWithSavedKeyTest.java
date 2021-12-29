package de.dominikschadow.javasecurity.tink.mac;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HmacShaWithSavedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final String KEYSET_FILENAME = "src/test/resources/keysets/hmac-sha.json";
    private final File keysetFile = new File(KEYSET_FILENAME);

    private HmacShaWithSavedKey hmac;

    @BeforeEach
    protected void setup() throws Exception {
        hmac = new HmacShaWithSavedKey();

        hmac.generateAndStoreKey(keysetFile);
    }

    @Test
    void unchangedInputValidatesSuccessful() throws Exception {
        KeysetHandle keysetHandle = hmac.loadKey(keysetFile);

        byte[] initialMac = hmac.computeMac(keysetHandle, INITIAL_TEXT);
        boolean validation = hmac.verifyMac(keysetHandle, initialMac, INITIAL_TEXT);

        Assertions.assertAll(
                () -> assertNotNull(initialMac),
                () -> assertTrue(validation)
        );
    }

    @Test
    void changedInputValidationFails() throws Exception {
        KeysetHandle keysetHandle = hmac.loadKey(keysetFile);

        byte[] initialMac = hmac.computeMac(keysetHandle, INITIAL_TEXT);
        boolean validation = hmac.verifyMac(keysetHandle, initialMac, "manipulation".getBytes(StandardCharsets.UTF_8));

        Assertions.assertAll(
                () -> assertNotNull(initialMac),
                () -> assertFalse(validation)
        );
    }
}