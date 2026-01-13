package com.operis.project.adapter.infrastructure.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JWTConnectedUserResolver {
    private final JWTParser JWTParser;

    public String extractConnectedUserEmail(String bearerToken) {
        JWTClaimsSet jwtClaimsSet = JWTParser.parseToken(bearerToken.substring(7));
        return jwtClaimsSet.getSubject();

    }
}
