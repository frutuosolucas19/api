package br.udesc;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PersonResourceTest {

    @Test
    public void testProtectedEndpointWithJwtFlow() {
        given()
                .when().get("/pessoa")
                .then()
                .statusCode(401);

        long iat = System.currentTimeMillis() / 1000;
        String token = Jwt.issuer("udesc-api")
                .subject("teste@mail.com")
                .upn("teste@mail.com")
                .groups(Set.of("User"))
                .issuedAt(iat)
                .expiresAt(iat + 3600)
                .sign();

        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/pessoa")
                .then()
                .statusCode(200);
    }

}

