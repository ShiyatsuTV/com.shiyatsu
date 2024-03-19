package com.shiyatsu.cipher.util;

import com.shiyatsu.cipher.exception.CipherException;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for encryption and decryption operations.
 * It uses AES algorithm with CBC mode and PKCS5Padding for encryption and decryption.
 */
public class CipherUtil {

	/**
	 * Logger service for logging errors and information.
	 */
	private static final ILoggerService logger = LoggerService.getLoggingService();

	/**
	 * The transformation for the cipher, specifying algorithm, mode, and padding.
	 */
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	/**
	 * The secret key for encryption and decryption.
	 */
	private static SecretKey secretKey = null;

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
	 * @param IvParameter The Base64 encoded IV parameter string.
	 * @return The byte array of the IV parameter.
	 */
	private static byte[] getIVParameterSpecFromString(String IvParameter) {
		return Base64.getDecoder().decode(IvParameter);
	}

	/**
	 * Encrypts a string input with the specified IV string.
	 *
	 * @param input The string to encrypt.
	 * @param IvStr The IV string used for encryption.
	 * @return The encrypted string, Base64 encoded.
	 * @throws CipherException If encryption fails or the secret key is not initialized.
	 */
	public static String encrypt(String input, String IvStr) throws CipherException {
		if (secretKey == null) {
			throw new CipherException("Secret key is not initialized");
		}
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(getIVParameterSpecFromString(IvStr));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			byte[] encryptedBytes = cipher.doFinal(input.getBytes());
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			logger.error(CipherUtil.class, "Fail to encrypt input", e);
			throw new CipherException("Fail to encrypt input : " + e.getMessage());
		}
	}

	/**
	 * Decrypts a Base64 encoded encrypted string with the specified IV string.
	 *
	 * @param encrypted The encrypted string to decrypt, Base64 encoded.
	 * @param IvStr The IV string used for decryption.
	 * @return The decrypted string.
	 * @throws CipherException If decryption fails or the secret key is not initialized.
	 */
	public static String decrypt(String encrypted, String IvStr) throws CipherException {
		if (secretKey == null) {
			throw new CipherException("Secret key is not initialized");
		}
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(getIVParameterSpecFromString(IvStr));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
			return new String(decryptedBytes);
		} catch (Exception e) {
			logger.error(CipherUtil.class, "Fail to decrypt input", e);
			throw new CipherException("Fail to decrypt input : " + e.getMessage());
		}
	}

	/**
	 * Generates an IV Parameter Spec.
	 *
	 * @return An IvParameterSpec instance with a randomly generated IV.
	 */
	private static IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}
}

