# awesomeEvents - Spring Boot Backend

A containerized Spring Boot backend application for managing events, with email notifications and QR code generation. This version uses a prebuilt Docker image from Docker Hub and runs using **Docker Compose**

--- 

## Quick start

Starting the application from a public docker image

> Docker Image:
> **`cotezzlapyx/awesome-events:latest`**

---

### Requirements

Make sure you have the following installed:
- [Docker](https://docks.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)

---

### Setup

In a new folder, create `compose.yaml` with the following content

```yaml
services:
  postgres:
    image: 'postgres:latest'
    environment:
      POSTGRES_DB: ${DB_NAME}
      POSTGRES_USER: ${DB_USERNAME}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    ports:
      - '5432:5432'
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${DB_USERNAME}"]
      interval: 5s
      timeout: 5s
      retries: 5
      start_period: 10s

  backend:
    image: cotezzlapyx/awesome-events:latest
    environment:
      DB_NAME: ${DB_NAME}
      DB_USERNAME: ${DB_USERNAME}
      DB_PASSWORD: ${DB_PASSWORD}
      MAIL_HOST: ${MAIL_HOST}
      MAIL_PORT: ${MAIL_PORT}
      MAIL_USERNAME: ${MAIL_USERNAME}
      MAIL_PASSWORD: ${MAIL_PASSWORD}
      MAIL_SENDER: ${MAIL_SENDER}
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
```

Then, in the same folder, create `.env` file with following the content.
In order for email service to work, you will need to adjust the configuration to your values.

```dotenv
DB_NAME=example
DB_USERNAME=postgres
DB_PASSWORD=securePassword123

MAIL_HOST=mail.example.com
MAIL_PORT=587
MAIL_USERNAME=user@example.com
MAIL_PASSWORD=securePassword123
MAIL_SENDER=user@example.com
```

---

### Deploy

Finally, run the following command in the terminal / CLI / etc. to start the server:

```bash
docker compose up -d
```

Now, API is available at `http://localhost:8080/` or another port if you changed it in the configuration.

If you want to stop the server and remove the image, run:

```bash
docker compose down
```

If you want to access logs, run:

```bash
docker compose logs -f
```

---

## Manual build

To build the image manually using the source code, you must follow the next instructions.

---

### Requirements

Make sure you have the following installed:
- [Docker](https://docks.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java SDK 17](https://openjdk.org/projects/jdk/17/)

---

### Building the software

---

#### Building a jar file

To build a jar file, run:

Linux & MacOS
```bash
./mvnw clean package
```

Windows
```bash
mvnw.cmd clean package
```

---

#### Building docker image

To build a docker image from a jar file, run:

```bash
docker compose build
```

---

###  Setup

Now, when all required files are built, you must set up the environment variables.

In project root folder, create `.env` file with the following content.
(In order for email service to work, you will need to adjust the configuration to your values.)

```dotenv
DB_NAME=example
DB_USERNAME=postgres
DB_PASSWORD=securePassword123

MAIL_HOST=mail.example.com
MAIL_PORT=587
MAIL_USERNAME=user@example.com
MAIL_PASSWORD=securePassword123
MAIL_SENDER=user@example.com
```

---

### Deploy

Finally, run the following command in the terminal / CLI / etc. to start the server:

```bash
docker compose up -d
```

Now, API is available at `http://localhost:8080/` or another port if you changed it in the configuration.

If you want to stop the server and remove the image, run:

```bash
docker compose down
```

If you want to access logs, run:

```bash
docker compose logs -f
```

---

## Additional info

---

### Environment configuration

To set up the environment manually, you can use these commands.
These are the example commands to do it:
- cmd: `set VAR_NAME=value`
- bash: `export VAR_NAME=value`
- powershell: `$env:VAR_NAME="value"`

Or you can create `.env` file in the main folder,
and configure everything there.

#### Environment variables

Some variables must be set up for the application to work properly.
Except the variables that have default values.

| Variable              | Description                                                     | Example                                 | Default                                 |
|-----------------------|-----------------------------------------------------------------|-----------------------------------------|-----------------------------------------|
| SERVER_PORT           | Port where the server is run                                    | 8080                                    | 8080                                    |
| DB_DRIVER             | Driver of used database                                         | org.postgresql.Driver                   | org.postgresql.Driver                   |
| DB_BASE               | Base source to your database                                    | postgresql                              | postgresql                              |
| DB_URL                | URL to your database                                            | postgres:5432                           | postgres:5432                           |
| DB_NAME               | Name of your database                                           | example-database                        | -                                       |
| DB_USERNAME           | Username to access your database                                | postgres                                | -                                       |
| DB_PASSWORD           | Password to access your database                                | securePassword123                       | -                                       |
| HIBERNATE_DIALECT     | Dialect of hibernate to work with your database                 | org.hibernate.dialect.PostgreSQLDialect | org.hibernate.dialect.PostgreSQLDialect |
| HIBERNATE_SHOW_SQL    | Show sql queries in output                                      | false                                   | true                                    |
| HIBERNATE_FORMAT_SQL  | Format sql queries in output                                    | false                                   | true                                    |
| DB_FLAG               | Database start-up flag (`update`, `create`, `validate`, `none`) | validate                                | create                                  |
| MAIL_HOST             | Your mail server host                                           | mail.example.net                        | -                                       |
| MAIL_PORT             | Your mail server port                                           | 587                                     | -                                       |
| MAIL_USERNAME         | Your mail username                                              | user@example.com                        | -                                       |
| MAIL_PASSWORD         | Your mail password                                              | securePassword123                       | -                                       |
| MAIL_SENDER           | Your mail address from which emails are sent                    | user@example.com                        | -                                       |
| MAIL_SMTP_AUTH        | Requires SMTP auth to send emails                               | true                                    | true                                    |
| MAIL_SMTP_STARTTLS    | Enable STARTTLS to secure the connection                        | true                                    | true                                    |
| QRCODE_SIZE           | Size of generated QR-Codes                                      | 500                                     | 500                                     |
| QRCODE_IMAGE_FORMAT   | Format of QR-Code image                                         | jpg                                     | jpg                                     |
| FRONTEND_URL          | URL to your frontend server                                     | http://localhost:3000                   | http://localhost:3000                   |
| TOKEN_EXPIRATION_TIME | Expiration time of security token (in days)                     | 3                                       | 1                                       |
| OTP_EXPIRATION_TIME   | Expiration time of one-time login code (in minutes)             | 60                                      | 15                                      |

---