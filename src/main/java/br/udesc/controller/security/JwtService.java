package br.udesc.controller.security;

import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;

import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class JwtService {
    public String gerarToken(String email, Long userId){
        long iat = System.currentTimeMillis()/1000;
        long exp = iat + 3600; // 1 hora

        return Jwt.issuer("udesc-api")
                .subject(email)
                .upn(email)
                .claim("uid", userId)
                .groups(Set.of("User"))
                .issuedAt(iat).expiresAt(exp)
                .sign();
    }
}
