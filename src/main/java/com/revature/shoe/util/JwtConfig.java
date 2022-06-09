package com.revature.shoe.util;

import com.revature.shoe.util.annotations.Inject;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

public class JwtConfig {
    private final String salt = "adsfkladsjfajfshammerafadklsjfkeyboardsasdjflkjas";
    private int expiration = 60 * 60 * 1000;
    private final Key signingKey;

    private final SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;

    public JwtConfig() {
        byte[] saltyBytes = DatatypeConverter.parseBase64Binary(salt);
        signingKey = new SecretKeySpec(saltyBytes, sigAlg.getJcaName());
    }

    public int getExpiration() {
        return expiration;
    }

    public Key getSigningKey() {
        return signingKey;
    }

    public SignatureAlgorithm getSigAlg() {
        return sigAlg;
    }
}

