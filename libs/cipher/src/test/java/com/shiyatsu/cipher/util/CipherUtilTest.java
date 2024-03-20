package com.shiyatsu.cipher.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CipherUtilTest {

    @Test
    void testEncryptDecrypt() throws Exception {
        String testKey = "1234567891564874";
        String testString = "Hello, World!";
        CipherUtil.initSecretKey(testKey);
        String ivParameterSpec = CipherUtil.getIvParameterSpecBase64();
        String encryptedString = CipherUtil.encrypt(testString, ivParameterSpec);
        assertNotNull(encryptedString);
        String decryptedString = CipherUtil.decrypt(encryptedString, ivParameterSpec);
        assertEquals(testString, decryptedString);
    }

}
