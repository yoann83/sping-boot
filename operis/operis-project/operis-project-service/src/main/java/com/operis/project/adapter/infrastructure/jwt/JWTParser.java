package com.operis.project.adapter.infrastructure.jwt;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import com.nimbusds.jose.JOSEException;

@Component
public class JWTParser {
    @Value("${code.secret}")
    private String codeSecret;

    public JWTClaimsSet parseToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token); // Peut lancer ParseException
            JWSVerifier verifier = new MACVerifier(codeSecret); // Peut lancer JOSEException

            // Si la signature est mauvaise, on lance une RuntimeException
            if (!signedJWT.verify(verifier)) {
                // Cette erreur va "traverser" le catch ci-dessous car ce n'est PAS une ParseException
                throw new RuntimeException("Invalid JWT signature");
            }

            return signedJWT.getJWTClaimsSet();

        } catch (ParseException | JOSEException e) {
            // On attrape ICI seulement si le token est illisible ou mal configuré
            throw new RuntimeException("Error parsing JWT token format", e);
        }
    }
}
