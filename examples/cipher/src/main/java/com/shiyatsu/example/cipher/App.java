package com.shiyatsu.example.cipher;

import com.shiyatsu.cipher.exception.CipherException;
import com.shiyatsu.cypher.util.CipherUtil;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

public class App {

	private static final ILoggerService logger = LoggerService.getLoggingService();

	public static void main(String[] args) throws CipherException {
		CipherUtil.initSecretKey("mySecretKey");
		String iv = CipherUtil.getIvParameterSpecBase64();

		String passwordToEncrypt = "Q!R6$eqLxxbnKoCR";
		String encryptedPassword = CipherUtil.encrypt(passwordToEncrypt, iv);

		logger.info(App.class, "Unencrypted password: " + passwordToEncrypt);
		logger.info(App.class, "Encrypted password: " + encryptedPassword);
		logger.info(App.class, "Decrypted password: " + CipherUtil.decrypt(encryptedPassword, iv));
	}

}
