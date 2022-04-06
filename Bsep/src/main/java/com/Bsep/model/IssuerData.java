package com.Bsep.model;

import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;
import java.security.PublicKey;

public class IssuerData {

    private PrivateKey privateKey;
    private X500Name x500name;
    private PublicKey publicKey;

    public IssuerData() {
    }

    public IssuerData(PrivateKey privateKey, X500Name x500name, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.x500name = x500name;
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public X500Name getX500name() {
        return x500name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
