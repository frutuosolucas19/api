# api Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/api-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- REST resources for Hibernate ORM with Panache ([guide](https://quarkus.io/guides/rest-data-panache)): Generate JAX-RS resources for your Hibernate Panache entities and repositories
- RESTEasy Classic JSON-B ([guide](https://quarkus.io/guides/rest-json)): JSON-B serialization support for RESTEasy Classic

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
## Environment variables

This project reads database and JWT settings from environment variables.

Required in all profiles (`dev`, `test`, `prod`):

- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_JDBC_URL` (example: `jdbc:postgresql://localhost:5433/api`)
- `JWT_PRIVATE_KEY_LOCATION` (file URI to private key PEM, e.g. `file:///C:/secure-path/privateKey.pem`)
- `JWT_PUBLIC_KEY_LOCATION` (file URI to public key PEM, e.g. `file:///C:/secure-path/publicKey.pem`)

Optional:

- `JWT_ISSUER` (default: `udesc-api`)
- `TEST_DB_CLEAN_AT_START` (default: `false`; use `true` only for isolated test DBs)

Use a local `.env` file (ignored by git) for development convenience. See `.env.example`.

## Database migrations

- Schema evolution is managed by Flyway migrations in `src/main/resources/db/migration`.
- `quarkus.hibernate-orm.schema-management.strategy=validate` is used to prevent implicit schema changes.
- Do not use `update` in production; every schema change must be versioned migration.

## Auth rules by profile

- `prod` (default behavior): only `/usuario/cadastro` and `/usuario/login` are public; all other routes require JWT.
- `dev`: additionally exposes `/`, `/index.html`, `/q/openapi`, `/q/swagger-ui/*` for local debugging.

## Secret hygiene

JWT key material was detected in git history (older commits). To fully sanitize the repository:

1. Rewrite history removing key files.
2. Force-push rewritten branches/tags.
3. Rotate all affected keys.

Example with `git filter-repo`:

```shell
git filter-repo --invert-paths --path src/main/resources/privateKey.pem --path src/main/resources/publicKey.pem
git push --force --all
git push --force --tags
```

## CI/CD and production secret rotation

After key rotation, update CI/CD and runtime secrets immediately.

CI/CD (GitHub):

1. Install/auth GitHub CLI: `gh auth login`.
2. Run:

```powershell
./scripts/rotate-ci-secrets.ps1 `
  -Repo "frutuosolucas19/api" `
  -PrivateKeyPath "C:\Users\lucas\.secrets\api-jwt\privateKey-20260311-104247.pem" `
  -PublicKeyPath "C:\Users\lucas\.secrets\api-jwt\publicKey-20260311-104247.pem" `
  -DbUsername "<db-user>" `
  -DbPassword "<db-pass>" `
  -DbJdbcUrl "jdbc:postgresql://<host>:5432/api" `
  -JwtIssuer "udesc-api"
```

Workflow note:

- Store key PEMs in secrets (`JWT_PRIVATE_KEY_PEM`, `JWT_PUBLIC_KEY_PEM`).
- During job execution, write them to temporary files and set:
  `JWT_PRIVATE_KEY_LOCATION` and `JWT_PUBLIC_KEY_LOCATION`.

Server/containers:

1. Copy new PEM files to a protected path outside the app repo.
2. Update env vars:
   - `JWT_PRIVATE_KEY_LOCATION`
   - `JWT_PUBLIC_KEY_LOCATION`
   - `DB_USERNAME`, `DB_PASSWORD`, `DB_JDBC_URL`, `JWT_ISSUER`
3. Restart application/container.
4. Revoke old keys and remove old files.
