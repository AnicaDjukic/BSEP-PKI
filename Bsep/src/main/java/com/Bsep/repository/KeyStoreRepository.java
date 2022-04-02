package com.Bsep.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Repository;

import com.Bsep.keystore.KeyStoreWriter;
import com.Bsep.model.CertificateType;

@Repository
public class KeyStoreRepository {
	private KeyStore keyStoreRoot;
	private KeyStore keyStoreIntermediate;
	private KeyStore keyStoreEndEntity;
	
	private final String KS_ROOT_PATH = "root.jks";
	private final String KS_INTERMEDIATE_PATH = "intermediate.jks";
	private final String KS_END_ENTITY_PATH = "endEntity.jks";
	
	private final String PASSWORD = "password";
	
	private KeyStoreWriter keyStoreWriter= new KeyStoreWriter();
	public KeyStoreRepository() {
		Security.addProvider(new BouncyCastleProvider());
		try {
			keyStoreRoot = KeyStore.getInstance("JKS");
			keyStoreIntermediate = KeyStore.getInstance("JKS");
			keyStoreEndEntity = KeyStore.getInstance("JKS");
			
			keyStoreWriter.loadKeyStore(KS_ROOT_PATH, PASSWORD.toCharArray(), keyStoreRoot);
			keyStoreWriter.loadKeyStore(KS_INTERMEDIATE_PATH, PASSWORD.toCharArray(), keyStoreIntermediate);
			keyStoreWriter.loadKeyStore(KS_END_ENTITY_PATH, PASSWORD.toCharArray(), keyStoreEndEntity);
			
			/*keyStoreWriter.saveKeyStore(KS_ROOT_PATH, PASSWORD.toCharArray(), keyStoreRoot);
			keyStoreWriter.saveKeyStore(KS_INTERMEDIATE_PATH, PASSWORD.toCharArray(), keyStoreIntermediate);
			keyStoreWriter.saveKeyStore(KS_END_ENTITY_PATH, PASSWORD.toCharArray(), keyStoreEndEntity);*/
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveCertificate(PrivateKey privateKey, X509Certificate certificate, CertificateType type) {
		
		if (type==CertificateType.ROOT) {
			keyStoreWriter.write(certificate.getSerialNumber().toString(), privateKey, PASSWORD.toCharArray(), certificate, keyStoreRoot);
			keyStoreWriter.saveKeyStore(KS_ROOT_PATH, PASSWORD.toCharArray(), keyStoreRoot);
		}
	}
	
	
}
