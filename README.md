# kerno_droid_api

Project to register and manage activities of devices belonging to farm.

## Setup

This project running with java 17+ and spring boot 3+.

You can clone the repository and import it into your favorite IDE.

- Java version in develop:  **17.0.8**  2023-07-18 LTS
- Maven version in develop: Apache Maven **3.8.6**
- Spring Boot version in develop: **3.1.2**
- DB version in develop: PostgresSQL **12**
- Docker db image: **debezium/postgres:12**

This project include docker-compose file to run postgres db in docker container.

## Run Server

You can run the server using your IDE or by using maven command:

```bash
mvn spring-boot:run
```

or by running the main class from your IDE.

Before execute this command, make sure you have a postgres db running and configured in your
**local.env** file, if you don't have this file, you can create it using
the [local.env.example](local.env.example) file as reference.

If all is correct, you should see the server running in the port configured in your
**local.env** file (default: 7783).

The database tables will be created automatically for integration with liquibase. Make sure the
database have uuid extension enabled and DB's created.

# Environment variables used in this project:

- **LOGGING_LEVEL:** Set the logging level for the application (default: INFO).
- **SPRING_PROFILES_ACTIVE:** Set the active Spring profile (default: dev).
- **DB_HOST:** Database host (default: localhost).
- **DB_PORT:** Database port (default: 5432).
- **DB_NAME:** Database name (default: farm_db).
- **DB_USERNAME:** Database username (default: dbusr).
- **DB_PASSWORD:** Database password (default: dbpwd).
- **SERVER_PORT_FARM:** Port on which the server will run (default: 7783).
- **APP_SERVER_URL:** Base URL of the application (default: http://localhost:7783).

Additionally, this project include a module to authenticate users using JWT tokens. Can configure in
this way:

- **DB_HOST_AUTH:** Database host for authentication (default: localhost).
- **DB_PORT_AUTH:** Database port for authentication (default: 5432).
- **DB_NAME_AUTH:** Database name for authentication (default: auth_db).
- **DB_USERNAME_AUTH:** Database username for authentication (default: auth_usr).
- **DB_PASSWORD_AUTH:** Database password for authentication (default: auth_pwd).
- **AUTH_SERVER_PORT:** Port on which the authentication server will run (default: 7784).
- **APP_SERVER_URL_AUTH:** Base URL of the authentication application (
  default: http://localhost:7784).
- **OAUTH2_JWT_SECRET:** Secret key for signing JWT (should be at least 32 characters long).
- **OAUTH2_JWT_AUDIENCE:** Audience for JWT (default: farm.api).
- **OAUTH2_JWT_AUTH_TOKEN_EXP:** Expiration time for auth tokens in seconds (default: 3600).
- **OAUTH2_JWT_AUTH_TOKEN_TYPE_EXP:** Way measure to access-token expiration (default:seconds)(
  accepted-values: milliseconds,seconds,minutes,hours,days).
- **OAUTH2_JWT_REFRESH_TOKEN_EXP:** Expiration time for refresh tokens in seconds (default: 86400).
- **OAUTH2_JWT_REFRESH_TOKEN_TYPE_EXP:** Way measure to refresh-token expiration (default:days)(
  accepted-values: milliseconds,seconds,minutes,hours,days).
- **OAUTH2_JWT_REFRESH_TOKEN_SECURE:** Flag to indicate if the refresh token httpOnly cookie should
  be secure and cross-site (default: dev)(accepted-values: dev,prod).
- **APP_CORS_ALLOWED_ORIGINS:** **(Important)** Comma-separated list of allowed origins for CORS
  (default: http://localhost:3000). Make sure to set this to the correct value for your frontend,
  swagger-ui, and any other clients that will access the API.

Finally, if you use IntelliJ IDEA, enable plugin "EnvFile" to load environment variables from
local.env file. If you use another IDE, check how to load environment variables from a file.