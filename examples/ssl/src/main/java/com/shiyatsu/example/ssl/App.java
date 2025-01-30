package com.shiyatsu.example.ssl;

import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;
import com.shiyatsu.ssl.exception.SslException;
import com.shiyatsu.ssl.util.ObjectIdentifier;
import com.shiyatsu.ssl.util.SslUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Optional;

public class App {

	private static final ILoggerService logger = LoggerService.getLoggingService();

	public static void main(String[] args) throws URISyntaxException, SslException {
		Security.addProvider(new BouncyCastleProvider());
		URL resource = App.class.getClassLoader().getResource("keystore");
		File file = new File(resource.toURI());

		ObjectIdentifier identifier = new ObjectIdentifier();
		identifier.setCountryCode("FR");
		identifier.setStateOrProvinceName("France");
		identifier.setOrganization("Shiyatsu");
		identifier.setOrganizationUnitName("Yatsu");
		identifier.setLocalityName("Paris");
		identifier.setCommonName("127.0.0.1");
		identifier.setEmailAddress("shiyatsu@shiyatsu.com");

		X509Certificate selfSignedCertificate = SslUtil.generateSelfSignedCertificate(identifier, Optional.empty());
		SslUtil.installCertificate(file, selfSignedCertificate, "Shiyatsu",  "changeit");
	}

}
