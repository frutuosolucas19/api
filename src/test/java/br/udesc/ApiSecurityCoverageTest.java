package br.udesc;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ApiSecurityCoverageTest {

    private static Stream<String> protectedListEndpoints() {
        return Stream.of(
                "/usuario/usuarios",
                "/pessoa/pessoas",
                "/resposta/respostas",
                "/pergunta/perguntas",
                "/local/pessoas",
                "/endereco/enderecos",
                "/forum/foruns",
                "/localizacao/localizacoes",
                "/denuncia/denuncias"
        );
    }

    private String authToken() {
        long iat = System.currentTimeMillis() / 1000;
        return Jwt.issuer("udesc-api")
                .subject("teste@mail.com")
                .upn("teste@mail.com")
                .groups(Set.of("User"))
                .issuedAt(iat)
                .expiresAt(iat + 3600)
                .sign();
    }

    @ParameterizedTest
    @MethodSource("protectedListEndpoints")
    void shouldRequireJwtForProtectedEndpoints(String endpoint) {
        given()
                .when().get(endpoint)
                .then()
                .statusCode(401);
    }

    @ParameterizedTest
    @MethodSource("protectedListEndpoints")
    void shouldAllowAccessWithJwtForProtectedEndpoints(String endpoint) {
        given()
                .header("Authorization", "Bearer " + authToken())
                .when().get(endpoint)
                .then()
                .statusCode(200);
    }

    @Test
    void shouldAllowPublicSignupWithoutJwt() {
        given()
                .contentType("application/json")
                .body("{}")
                .when().post("/usuario")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldAllowPublicLoginWithoutJwt() {
        given()
                .contentType("application/json")
                .body("{}")
                .when().post("/usuario/login")
                .then()
                .statusCode(400);
    }
}
