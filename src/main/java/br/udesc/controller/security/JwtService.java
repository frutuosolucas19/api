package br.udesc.controller.security;

import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class JwtService {
    private static final String RECUPERACAO_SENHA = "password-reset";

    @Inject
    JWTParser jwtParser;

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

    public String gerarTokenRecuperacaoSenha(String email) {
        long iat = System.currentTimeMillis() / 1000;
        long exp = iat + 900; // 15 minutos

        return Jwt.issuer("udesc-api")
                .subject(email)
                .upn(email)
                .claim("purpose", RECUPERACAO_SENHA)
                .issuedAt(iat)
                .expiresAt(exp)
                .sign();
    }

    public String validarEExtrairEmailTokenRecuperacaoSenha(String tokenAws) {
        try {
            JsonWebToken jwt = jwtParser.parse(tokenAws);
            Object purpose = jwt.getClaim("purpose");
            if (purpose == null || !RECUPERACAO_SENHA.equals(String.valueOf(purpose))) {
                return null;
            }
            String email = jwt.getSubject();
            if (email == null || email.isBlank()) {
                return null;
            }
            return email.trim().toLowerCase();
        } catch (Exception ignored) {
            return null;
        }
    }
}
