package com.shiyatsu.cypher.util;

import java.security.SecureRandom;

/**
 * Utility class for generating secret keys.
 * This class provides functionality to generate random strings which can be used as secret keys.
 * The generated strings are composed of uppercase letters, lowercase letters, and digits.
 *
 * Usage example:
 * String secretKey = SecretKeyGeneratorUtil.generateRandomString();
 */
public class SecretKeyGeneratorUtil {

    /**
     * A string containing the characters that can be used in the generated secret key.
     * Includes uppercase letters, lowercase letters, and digits.
     */
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * The maximum length of the generated secret key string.
     */
    private static final int maxLength = 128;

    /**
     * Generates a random string of a predefined length to be used as a secret key.
     * This method uses {@link SecureRandom} to generate a secure random string of {@link #maxLength} characters.
     * Each character is randomly chosen from the predefined set of characters in {@link #CHARACTERS}.
     *
     * @return A securely generated random string of {@link #maxLength} characters.
     */
    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(maxLength);
        for (int i = 0; i < maxLength; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

}
