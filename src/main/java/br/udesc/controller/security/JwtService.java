package br.udesc.controller.security;

import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@ApplicationScoped
public class JwtService {
    private static final Logger LOG = Logger.getLogger(JwtService.class);
    private static final String RECUPERACAO_SENHA = "password-reset";
    private static final String ISSUER = "udesc-api";
    private static final long TOKEN_EXPIRY_SECONDS = 3600;       // 1 hora
    private static final long RESET_TOKEN_EXPIRY_SECONDS = 900;  // 15 minutos

    @Inject
    JWTParser jwtParser;

    public String gerarToken(String email, Long userId){
        long iat = System.currentTimeMillis()/1000;
        long exp = iat + TOKEN_EXPIRY_SECONDS;

        return Jwt.issuer(ISSUER)
                .subject(email)
                .upn(email)
                .claim("uid", userId)
                .groups(Set.of("User"))
                .issuedAt(iat).expiresAt(exp)
                .sign();
    }

    public String gerarTokenRecuperacaoSenha(String email) {
        long iat = System.currentTimeMillis() / 1000;
        long exp = iat + RESET_TOKEN_EXPIRY_SECONDS;

        return Jwt.issuer(ISSUER)
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
        } catch (Exception e) {
            LOG.warn("Falha ao validar token de recuperacao de senha: " + e.getMessage());
            return null;
        }
    }
}
