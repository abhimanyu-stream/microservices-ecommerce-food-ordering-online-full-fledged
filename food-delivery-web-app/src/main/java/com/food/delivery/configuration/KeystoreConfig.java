package com.food.delivery.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class KeystoreConfig {

	private static final Logger log = LoggerFactory.getLogger(KeystoreConfig.class);

	//@Value("${app.jwt.keyStorePKCE12-location}")
	//private String keyStorePKCE12Path;NOTE PLEASE SET app.jwt.keyStorePKCE12-location=classpath:/keys/keystore.p12 PROPERLY , ITS NOT READING THIS FILE, SO i DIRECTLY GIVEN PATH BELOW INTO FileInputStream ARGUMENT
	


	@Value("${app.jwt.keyStorePKCE12-password}")
	private String keyStorePKCE12Password;

	@Value("${app.jwt.destPKCE12-alias}")
	private String destkeyPKCE12Alias;

	private static FileInputStream fs = null;
	private static KeyStore keystore = null;
	
	private static Certificate cert = null;
	private static KeyPair keyPair = null;
	private static RSAPrivateKey rsaPrivateKey = null;
	private static RSAPublicKey rsaPublicKey = null;

	
	@Bean
	public RSAPublicKey getRSAPublicKey() {
		return rsaPublicKey;
	}

	@Bean
	public RSAPrivateKey getJwtSigningPrivateKey() {

		try {
			fs = new FileInputStream("C:\\Users\\acode\\Downloads\\ws-jobportal-unnati\\applicant-service\\src\\main\\resources\\keys\\keystore.p12");
			//fs = new FileInputStream(keyStorePKCE12Path);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

		try {
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e1) {
			
			e1.printStackTrace();
		}
		try {
			keystore.load(fs, keyStorePKCE12Password.toCharArray());// initializing keystore
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			
			e.printStackTrace();
		}

		try {
			rsaPrivateKey = (RSAPrivateKey) keystore.getKey(destkeyPKCE12Alias, keyStorePKCE12Password.toCharArray());
			System.out.println("Private key " + rsaPrivateKey);
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}

		if (rsaPrivateKey instanceof RSAPrivateKey) {
			// Get certificate of public key

			try {
				cert = keystore.getCertificate(destkeyPKCE12Alias);
			} catch (KeyStoreException e) {
				
				e.printStackTrace();
			}

			// Get public key
			rsaPublicKey = (RSAPublicKey) cert.getPublicKey();

			// jwtDecoder(RSAPublicKey rsaPublicKey)
			jwtDecoder(rsaPublicKey);

			// Return a key pair
			keyPair = new KeyPair(rsaPublicKey, rsaPrivateKey);
		}
		System.out.println("PrivateKey is " + rsaPrivateKey);

		return rsaPrivateKey;

	}

	
	 
	  @Bean 
	  public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) { 
		  
		  return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
	  }
	 
}
