package br.udesc;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ApiSecurityCoverageTest {

    private static Stream<String> protectedListEndpoints() {
        return Stream.of(
                "/pessoa",
                "/resposta",
                "/pergunta",
                "/local",
                "/endereco",
                "/forum",
                "/localizacao",
                "/denuncia"
        );
    }

    private static Stream<String> protectedPutEndpoints() {
        return Stream.of(
                "/pessoa/999999",
                "/resposta/999999",
                "/pergunta/999999",
                "/local/999999",
                "/endereco/999999",
                "/forum/999999",
                "/localizacao/999999",
                "/denuncia/999999",
                "/usuario/999999"
        );
    }

    private static Stream<String> protectedPutNotFoundEndpoints() {
        return Stream.of(
                "/pessoa/999999",
                "/resposta/999999",
                "/pergunta/999999",
                "/local/999999",
                "/endereco/999999",
                "/forum/999999",
                "/localizacao/999999"
        );
    }

    private static Stream<String> protectedPostEndpoints() {
        return Stream.of(
                "/pessoa",
                "/resposta",
                "/pergunta",
                "/local",
                "/endereco",
                "/forum",
                "/localizacao",
                "/denuncia"
        );
    }

    private static Stream<Arguments> legacyAliases() {
        return Stream.of(
                Arguments.of("/usuario", "/usuario/usuarios"),
                Arguments.of("/pessoa", "/pessoa/pessoas"),
                Arguments.of("/resposta", "/resposta/respostas"),
                Arguments.of("/pergunta", "/pergunta/perguntas"),
                Arguments.of("/local", "/local/pessoas"),
                Arguments.of("/endereco", "/endereco/enderecos"),
                Arguments.of("/forum", "/forum/foruns"),
                Arguments.of("/localizacao", "/localizacao/localizacoes"),
                Arguments.of("/denuncia", "/denuncia/denuncias")
        );
    }

    private String authToken() {
        return authToken(Set.of("User"));
    }

    private String authToken(Set<String> groups) {
        long iat = System.currentTimeMillis() / 1000;
        return Jwt.issuer("udesc-api")
                .subject("teste@mail.com")
                .upn("teste@mail.com")
                .groups(groups)
                .issuedAt(iat)
                .expiresAt(iat + 3600)
                .sign();
    }

    private String tokenRecuperacaoSenha(String email) {
        long iat = System.currentTimeMillis() / 1000;
        return Jwt.issuer("udesc-api")
                .subject(email)
                .upn(email)
                .claim("purpose", "password-reset")
                .issuedAt(iat)
                .expiresAt(iat + 900)
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

    @ParameterizedTest
    @MethodSource("protectedListEndpoints")
    void shouldReturnUnauthorizedWithMalformedJwt(String endpoint) {
        given()
                .header("Authorization", "Bearer not-a-valid-jwt")
                .when().get(endpoint)
                .then()
                .statusCode(401);
    }

    @ParameterizedTest
    @MethodSource("protectedListEndpoints")
    void shouldReturnForbiddenWithWrongRole(String endpoint) {
        given()
                .header("Authorization", "Bearer " + authToken(Set.of("Guest")))
                .when().get(endpoint)
                .then()
                .statusCode(403);
    }

    @ParameterizedTest
    @MethodSource("legacyAliases")
    void shouldKeepLegacyAliasWorking(String canonical, String legacy) {
        String token = authToken();
        int canonicalStatus = given()
                .header("Authorization", "Bearer " + token)
                .when().get(canonical)
                .then()
                .extract().statusCode();

        given()
                .header("Authorization", "Bearer " + token)
                .when().get(legacy)
                .then()
                .statusCode(canonicalStatus);
    }

    @ParameterizedTest
    @MethodSource("protectedPutEndpoints")
    void shouldRequireJwtForProtectedPut(String endpoint) {
        given()
                .contentType("application/json")
                .body("{}")
                .when().put(endpoint)
                .then()
                .statusCode(401);
    }

    @ParameterizedTest
    @MethodSource("protectedPutNotFoundEndpoints")
    void shouldReturnNotFoundForPutWhenEntityDoesNotExist(String endpoint) {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken())
                .body("{}")
                .when().put(endpoint)
                .then()
                .statusCode(404);
    }

    @ParameterizedTest
    @MethodSource("protectedPostEndpoints")
    void shouldRequireJwtForProtectedPost(String endpoint) {
        given()
                .contentType("application/json")
                .body("{}")
                .when().post(endpoint)
                .then()
                .statusCode(401);
    }

    @Test
    void shouldRequireJwtForProtectedDelete() {
        given()
                .when().delete("/usuario/999999")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldAllowPublicSignupWithoutJwt() {
        given()
                .contentType("application/json")
                .body("{}")
                .when().post("/usuario/cadastro")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldCreatePessoaWith201AndLocation() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken())
                .body(Map.of("nome", "Person API", "imagem", "img.png"))
                .when().post("/pessoa")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .header("Location", containsString("/pessoa/"));
    }

    @Test
    void shouldCreateLocalizacaoWith201AndLocation() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken())
                .body(Map.of("latitude", "-27.59", "longitude", "-48.55"))
                .when().post("/localizacao")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .header("Location", containsString("/localizacao/"));
    }

    @Test
    void shouldCreateEnderecoWith201AndLocation() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken())
                .body(Map.of(
                        "logradouro", "Rua Teste",
                        "cidade", "Florianopolis",
                        "uf", "SC"
                ))
                .when().post("/endereco")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .header("Location", containsString("/endereco/"));
    }

    @Test
    void shouldRequireJwtForUserListWithoutToken() {
        given()
                .when().get("/usuario")
                .then()
                .statusCode(401);
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

    @Test
    void shouldAllowForgotPasswordWithoutJwtAndValidatePayload() {
        given()
                .contentType("application/json")
                .body("{}")
                .when().post("/usuario/esqueci-senha")
                .then()
                .statusCode(400)
                .body("erro", equalTo("Email obrigatorio."));
    }

    @Test
    void shouldReturnNotFoundForForgotPasswordWhenUserDoesNotExist() {
        given()
                .contentType("application/json")
                .body(Map.of("email", "nao.existe+" + System.currentTimeMillis() + "@mail.com"))
                .when().post("/usuario/esqueci-senha")
                .then()
                .statusCode(404)
                .body("erro", equalTo("User nao encontrado."));
    }

    @Test
    void shouldReturnNoContentForForgotPasswordWhenUserExists() {
        String email = "forgot.usuario+" + System.currentTimeMillis() + "@mail.com";
        String senha = "Senha@123";

        given()
                .contentType("application/json")
                .body(Map.of(
                        "email", email,
                        "senha", senha,
                        "tipoUsuario", "normal",
                        "pessoa", Map.of("nome", "Forgot User")
                ))
                .when().post("/usuario/cadastro")
                .then()
                .statusCode(201);

        given()
                .contentType("application/json")
                .body(Map.of("email", email))
                .when().post("/usuario/esqueci-senha")
                .then()
                .statusCode(204);
    }

    @Test
    void shouldReturnBadRequestForInvalidResetPasswordToken() {
        given()
                .contentType("application/json")
                .body(Map.of("tokenAws", "token-invalido", "novaSenha", "Senha@123"))
                .when().post("/usuario/redefinir-senha")
                .then()
                .statusCode(400)
                .body("erro", equalTo("Token invalido ou expirado."));
    }

    @Test
    void shouldResetPasswordWithValidToken() {
        String email = "reset.usuario+" + System.currentTimeMillis() + "@mail.com";
        String senhaAntiga = "Senha@123";
        String senhaNova = "Senha@1234";

        given()
                .contentType("application/json")
                .body(Map.of(
                        "email", email,
                        "senha", senhaAntiga,
                        "tipoUsuario", "normal",
                        "pessoa", Map.of("nome", "Reset User")
                ))
                .when().post("/usuario/cadastro")
                .then()
                .statusCode(201);

        String tokenAws = tokenRecuperacaoSenha(email);

        given()
                .contentType("application/json")
                .body(Map.of("tokenAws", tokenAws, "novaSenha", senhaNova))
                .when().post("/usuario/redefinir-senha")
                .then()
                .statusCode(204);

        given()
                .contentType("application/json")
                .body(Map.of("email", email, "senha", senhaAntiga))
                .when().post("/usuario/login")
                .then()
                .statusCode(401);

        given()
                .contentType("application/json")
                .body(Map.of("email", email, "senha", senhaNova))
                .when().post("/usuario/login")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldSignupAndLoginSuccessfullyWithoutToken() {
        String email = "novo.usuario+" + System.currentTimeMillis() + "@mail.com";
        String senha = "Senha@123";

        given()
                .contentType("application/json")
                .body(Map.of(
                        "email", email,
                        "senha", senha,
                        "tipoUsuario", "normal",
                        "pessoa", Map.of("nome", "Novo User")
                ))
                .when().post("/usuario/cadastro")
                .then()
                .statusCode(201);

        given()
                .contentType("application/json")
                .body(Map.of("email", email, "senha", senha))
                .when().post("/usuario/login")
                .then()
                .statusCode(200);
    }
}

