package com.shiyatsu.cypher.util;

import com.shiyatsu.cipher.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for encryption and decryption operations.
 * It uses AES algorithm with CBC mode and PKCS5Padding for encryption and decryption.
 */
public class CipherUtil {
	/**
	 * The transformation for the cipher, specifying algorithm, mode, and padding.
	 */
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";

	/**
	 * The secret key for encryption and decryption.
	 */
	private static SecretKey secretKey = null;

	private CipherUtil() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	/**
	 * Initializes the secret key with a specified string.
	 * The key is encoded using Base64.
	 *
	 * @param key The string to use for the secret key.
	 */
	public static void initSecretKey(String key) {
		secretKey = new SecretKeySpec(Base64.getEncoder().encode(key.getBytes()), "AES");
	}

	/**
	 * Generates a Base64 encoded string of the IV Parameter Spec.
	 *
	 * @return A Base64 encoded string of the IV parameter spec.
	 */
	public static String getIvParameterSpecBase64() {
		return Base64.getEncoder().encodeToString(generateIv().getIV());
	}

	/**
	 * Converts a Base64 encoded IV parameter string back to a byte array.
	 *
	 * @param ivParameter The Base64 encoded IV parameter string.
	 * @return The byte array of the IV parameter.
	 */
	private static byte[] getIVParameterSpecFromString(String ivParameter) {
		return Base64.getDecoder().decode(ivParameter);
	}

	/**
	 * Encrypts a string input with the specified IV string.
	 *
	 * @param input The string to encrypt.
	 * @param ivStr The IV string used for encryption.
	 * @return The encrypted string, Base64 encoded.
	 * @throws CipherException If encryption fails or the secret key is not initialized.
	 */
	public static String encrypt(String input, String ivStr) throws CipherException {
		if (secretKey == null) {
			throw new CipherException("Secret key is not initialized");
		}
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, getIVParameterSpecFromString(ivStr));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
			byte[] encryptedBytes = cipher.doFinal(input.getBytes());
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new CipherException("Fail to encrypt input : " + e.getMessage(), e);
		}
	}

	/**
	 * Decrypts a Base64 encoded encrypted string with the specified IV string.
	 *
	 * @param encrypted The encrypted string to decrypt, Base64 encoded.
	 * @param ivStr The IV string used for decryption.
	 * @return The decrypted string.
	 * @throws CipherException If decryption fails or the secret key is not initialized.
	 */
	public static String decrypt(String encrypted, String ivStr) throws CipherException {
		if (secretKey == null) {
			throw new CipherException("Secret key is not initialized");
		}
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, getIVParameterSpecFromString(ivStr)); // 128-bit auth tag length
			cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
			return new String(decryptedBytes);
		} catch (Exception e) {
			throw new CipherException("Fail to decrypt input : " + e.getMessage());
		}

	}

	/**
	 * Generates an IV Parameter Spec.
	 *
	 * @return An IvParameterSpec instance with a randomly generated IV.
	 */
	private static GCMParameterSpec generateIv() {
		byte[] iv = new byte[128];
		new SecureRandom().nextBytes(iv);
		return new GCMParameterSpec(128, iv);
	}
}

