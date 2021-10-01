package org.mycompany.utils;

import no.ks.fiks.maskinporten.Maskinportenklient;
import no.ks.fiks.maskinporten.MaskinportenklientProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class Maskinporten {
    public static String getToken(String filename, String pwd, String alias, String issuer, String audience, String tokenEndpoint, String scope) throws Exception {
        String keyStoreFilename = filename;
        char[] keyStorePassword = pwd.toCharArray();
        
        KeyStore keyStore = getKeyStore(keyStoreFilename, keyStorePassword);

        Maskinportenklient maskinporten = new Maskinportenklient(keyStore, alias, keyStorePassword, MaskinportenklientProperties.builder()
                .numberOfSecondsLeftBeforeExpire(10)
                .issuer(issuer)
                .audience(audience)
                .tokenEndpoint(tokenEndpoint)
                .build());

        String accessToken = maskinporten.getAccessToken(scope);
        return accessToken;
    }

    public static KeyStore getKeyStore(String keyStoreFilename, char[] keyStorePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(new FileInputStream(keyStoreFilename), keyStorePassword);
        return keyStore;
    }
}