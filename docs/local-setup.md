# Local setup guide

This project needs a small amount of local environment setup before it can run successfully. The biggest gaps are intentional: the main Spring `application.properties` file is gitignored, and the Maven wrapper metadata directory is not checked in.

## Prerequisites

- Java 17
- Maven 3.8+ available on your `PATH`
- MySQL or another compatible database instance you can point Spring Boot at
- Network access for any external APIs you plan to exercise, especially PayPal sandbox or live endpoints

## Repository-specific caveats

### 1. Maven wrapper is incomplete

The repository includes `mvnw` and `mvnw.cmd`, but the `.mvn` directory is not present. That means the wrapper cannot bootstrap Maven by itself.

Use:

```powershell
mvn test
mvn spring-boot:run
```

If you want to use the wrapper instead, restore the missing `.mvn\wrapper\...` files first.

### 2. Application config is local-only

`.gitignore` excludes:

- `src/main/resources/application.properties`
- `src/test/resources/application.properties`

Create at least `src/main/resources/application.properties` locally before starting the app.

### 3. A local JAR is part of the build

The Maven build references this checked-in file:

```text
lib\leedox-1.0-SNAPSHOT.jar
```

Make sure it stays in place. The `pom.xml` uses it as a system-scoped dependency.

## Suggested `application.properties`

The exact values are not checked into the repo, but the codebase shows that you need:

- a datasource for Spring Data JPA
- PayPal credentials under the `paypal.*` prefix
- enough schema support for Spring Security remember-me tokens because `SecurityConfig` wires a JDBC token repository

Use this as a starting point and replace values for your machine:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springmv?serverTimezone=UTC
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

paypal.base-url=https://api-m.sandbox.paypal.com
paypal.client-id=YOUR_PAYPAL_CLIENT_ID
paypal.secret=YOUR_PAYPAL_SECRET
```

## Database expectations

The repository uses Spring Data JPA repositories for members, words, books, games, players, matches, roles, and orders. There are no checked-in SQL bootstrap files in the repo, so you may need to:

1. create the database yourself
2. choose the schema name used by your local datasource URL
3. seed local data manually if you want the richer views to show realistic content

Because remember-me uses JDBC persistence, ensure the database user can create or update the required token table, or create it ahead of time if your environment disables schema changes.

## Running the app

From the repository root:

```powershell
mvn test
mvn spring-boot:run
```

Then open the app in your browser at the default Spring Boot port unless you override it in local config:

```text
http://localhost:8080/
```

Useful entry points:

- `/`
- `/wordbook/intro`
- `/book/intro`
- `/club/intro`

## Troubleshooting

### `MavenWrapperMain` or wrapper errors

Use `mvn` instead of `mvnw` unless you have restored the missing `.mvn` directory.

### Datasource startup failures

Check:

- the database server is running
- the JDBC URL, username, and password are correct
- the selected schema exists
- the MySQL driver version in `pom.xml` matches your server well enough

### PayPal errors

Check:

- `paypal.base-url`, `paypal.client-id`, and `paypal.secret`
- whether you are using sandbox credentials with the sandbox base URL
- outbound network access from your machine

### Login or remember-me persistence issues

If login works but remember-me does not, verify the datasource is working and that the token table required by Spring Security can be created or updated in your database.
