package com.operis.project.adapter.infrastructure.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.text.ParseException;

@RequiredArgsConstructor
@Component
public class JWTConnectedUserResolver {
    private final JWTParser JWTParser;

    // Méthode pour extraire l'email au lieu du subject
    public String extractConnectedUserEmail(String bearerToken) {
        try {
            JWTClaimsSet jwtClaimsSet = JWTParser.parseToken(bearerToken.substring(7));

            // On va chercher le champ "email"
            String email = jwtClaimsSet.getStringClaim("email");

            // Sécurité : Si l'email est vide (token mal formé), on peut renvoyer le subject par défaut ou lancer une erreur
            if (email == null) {
                throw new RuntimeException("Email claim is missing in the token");
            }

            return email;

        } catch (ParseException e) {
            throw new RuntimeException("Invalid Token claims", e);
        }
    }
}