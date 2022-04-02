package com.Bsep.repository;

import com.Bsep.keystore.KeyStoreWriter;
import com.Bsep.model.CertificateType;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Repository
public class KeyStoreRepository {
    private KeyStore keyStoreRoot;
    private KeyStore keyStoreIntermediate;
    private KeyStore keyStoreEndEntity;

    private final String KS_ROOT_PATH = "root.jks";
    private final String KS_INTERMEDIATE_PATH = "intermediate.jks";
    private final String KS_END_ENTITY_PATH = "endEntity.jks";

    private final String PASSWORD = "password";

    private KeyStoreWriter keyStoreWriter = new KeyStoreWriter();

    public KeyStoreRepository() {
        Security.addProvider(new BouncyCastleProvider());
        try {
            keyStoreRoot = KeyStore.getInstance("JKS");
            keyStoreIntermediate = KeyStore.getInstance("JKS");
            keyStoreEndEntity = KeyStore.getInstance("JKS");

            initKeyStore(KS_ROOT_PATH, keyStoreRoot);
            initKeyStore(KS_INTERMEDIATE_PATH, keyStoreIntermediate);
            initKeyStore(KS_END_ENTITY_PATH, keyStoreEndEntity);

        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void initKeyStore(String keyStorePath, KeyStore keyStore) throws IOException, NoSuchAlgorithmException, CertificateException {
        File rootKeyStoreFile = new File(keyStorePath);
        if (!rootKeyStoreFile.exists()) {
            keyStore.load(null, PASSWORD.toCharArray());
            keyStoreWriter.saveKeyStore(keyStorePath, PASSWORD.toCharArray(), keyStore);
        } else {
            keyStoreWriter.loadKeyStore(keyStorePath, PASSWORD.toCharArray(), keyStore);
        }
    }

    public void saveCertificate(PrivateKey privateKey, X509Certificate certificate, CertificateType type) {

        if (type == CertificateType.ROOT) {
            keyStoreWriter.write(certificate.getSerialNumber().toString(), privateKey, PASSWORD.toCharArray(), certificate, keyStoreRoot);
            keyStoreWriter.saveKeyStore(KS_ROOT_PATH, PASSWORD.toCharArray(), keyStoreRoot);
        }
    }


}
