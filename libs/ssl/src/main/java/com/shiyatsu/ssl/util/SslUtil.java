package com.shiyatsu.ssl.util;

import com.shiyatsu.ssl.exception.SslException;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class SslUtil {

    private static final String ENCRYPT = "SHA256WithRSAEncryption";

    private SslUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void installCertificate(File keystoreFile, X509Certificate cert, String entry,  String password) throws SslException {
        try {
            char[] passwordArrChar = password.toCharArray();
            KeyStore ks = initializeKeyStore(keystoreFile, password);
            KeyPair keyPair = generateKeyPair();
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = cert;
            ks.setKeyEntry(entry, keyPair.getPrivate(), passwordArrChar, chain);
            OutputStream stream = new FileOutputStream(keystoreFile);
            ks.store(stream, passwordArrChar);
            stream.close();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            throw new SslException(String.format("Fail to install certificate : %s", e.getMessage()), e);
        }

    }

    public static X509Certificate generateSelfSignedCertificate(ObjectIdentifier identifier, Optional<KeyPair> optionalKeyPair) throws SslException {
        try {
            KeyPair keyPair = optionalKeyPair.orElse(generateKeyPair());
            X500NameBuilder builder = getNameBuilder(identifier);
            ContentSigner sigGen = new JcaContentSignerBuilder(ENCRYPT).setProvider(BouncyCastleProvider.PROVIDER_NAME).build(keyPair.getPrivate());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 10);

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(builder.build(),
                    BigInteger.valueOf(System.currentTimeMillis()), new Date(System.currentTimeMillis() - 50000),
                    cal.getTime(), builder.build(), keyPair.getPublic());

            GeneralName name = getGeneralName(identifier.getCommonName());
            GeneralNames subjectAltName = new GeneralNames(name);
            certGen.addExtension(Extension.subjectAlternativeName, false, subjectAltName);
            X509Certificate cert = new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(certGen.build(sigGen));

            cert.checkValidity(new Date());
            cert.verify(keyPair.getPublic());
            cert.verify(cert.getPublicKey());

            return cert;
        } catch (CertIOException | CertificateException | OperatorCreationException | NoSuchAlgorithmException | SignatureException | InvalidKeyException | NoSuchProviderException e) {
            throw new SslException(String.format("Fail to generate self signed certificate : %s", e.getMessage()), e);
        }

    }

    public static KeyPair generateKeyPair() throws SslException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
            keyPairGenerator.initialize(4096, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new SslException(String.format("Fail to generate keypair : %s", e.getMessage()), e);
        }

    }

    private static X500NameBuilder getNameBuilder(ObjectIdentifier identifier) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        if (isNonEmpty(identifier.getCountryCode())) {
            builder.addRDN(BCStyle.C, identifier.getCountryCode());
        }
        if (isNonEmpty(identifier.getOrganization())) {
            builder.addRDN(BCStyle.O, identifier.getOrganization());
        }
        if (isNonEmpty(identifier.getOrganizationUnitName())) {
            builder.addRDN(BCStyle.OU, identifier.getOrganizationUnitName());
        }
        if (isNonEmpty(identifier.getLocalityName())) {
            builder.addRDN(BCStyle.L, identifier.getLocalityName());
        }
        if (isNonEmpty(identifier.getStateOrProvinceName())) {
            builder.addRDN(BCStyle.ST, identifier.getStateOrProvinceName());
        }
        if (isNonEmpty(identifier.getCommonName())) {
            builder.addRDN(BCStyle.CN, identifier.getCommonName());
        }
        if (isNonEmpty(identifier.getEmailAddress())) {
            builder.addRDN(BCStyle.E, identifier.getEmailAddress());
        }
        return builder;
    }

    private static KeyStore initializeKeyStore(File keystoreFile, String password) throws SslException {
        try {
            char[] passwordArrChar = password.toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            if (keystoreFile.exists()) {
                try (InputStream stream = new FileInputStream(keystoreFile)) {
                    ks.load(stream, passwordArrChar);
                }
            } else {
                ks.load(null, passwordArrChar);
            }
            return ks;
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e) {
            throw new SslException(String.format("Fail to initialize keystore : %s", e.getMessage()), e);
        }

    }

    private static GeneralName getGeneralName(String host) {
        GeneralName name = null;
        try {
            name = new GeneralName(GeneralName.iPAddress, host);
        } catch (IllegalArgumentException ex) {
            name = new GeneralName(GeneralName.dNSName, host);
        }
        return name;
    }

    private static boolean isNonEmpty(String valueStr) {
        return valueStr != null && valueStr.trim().isEmpty() == false;
    }
}
