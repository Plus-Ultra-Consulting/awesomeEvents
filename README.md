# awesomeEvents - Spring Boot Backend

A containerized Spring Boot backend application for managing events, with email notifications and QR code generation. This version uses a prebuilt Docker image from Docker Hub and runs using **Docker Compose**

--- 

## Docker Hub Image
> Docker Image:
> **`cotezzlapyx/awesome-events-backend:latest`**

---

## Requirements

Make sure you have the following installed:
- [Docker](https://docks.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)

---

## Environment Configuration

Set up the environment
example commands to do it:
- cmd: `set VAR_NAME=value`
- bash: `export VAR_NAME=value`
- powershell: `$env:VAR_NAME="value"`

### Variables to set up

All variables must be set up  for the program to work properly.
Except the variables that are marked optional and have default values.

```bash
# Server
SERVER_PORT # Port where the server is run (default: 8080), example: 8080

# Database
DB_DRIVER # Driver of used database, example: org.postgresql.Driver
DB_BASE # Base source to your database, example: postgresql
DB_URL # URL to your database, example: postgres:5432
DB_URL # Name of your database, example: example_database
DB_USERNAME # Username to access your database, example: postgres
DB_PASSWORD # Password to access your database, example: securePassword123
HIBERNATE_DIALECT # Dialect of hibernate to work with your database, example: org.hibernate.dialect.PostgreSQLDialect

## Optional
HIBERNATE_SHOW_SQL # Show sql queries in output (default: true), example: false
HIBERNATE_FORMAT_SQL # Format sql queries in output (default: true), example: false
DB_FLAG # Database start-up flag, example: create
### (`update` - updates database to match required structure)
### (`create` - creates new database with blank tables and etc, deletes all data)
### (`validate` - checks if database matches required form)
### (`none` - assumes that database is ready and matches the required structure)

# Mail
MAIL_HOST # Your mail server host, example: mail.example.net
MAIL_PORT # Your mail server port, example: 587
MAIL_USERNAME # Your mail username, example: user@example.com
MAIL_PASSWORD # Your mail password, example: securePassword123
MAIL_SENDER # Your mail address from what emails are sent, example: user@example.com

## Optional
MAIL_SMTP_AUTH # Requires auth or not to send emails (default: true), example: true
MAIL_SMTP_STARTTLS # Enable or not STARTTLS to secure the connection (default: true), example: true

# QR Code
## Optional
QRCODE_SIZE # Size of generated QR-Codes (default: 500), example: 500
QRCODE_IMAGE_FORMAT # Format of QR-Code image (default: jpg), example: jpg

#Frontend
FRONTEND_URL # URL to your frontend server, example: http://localhost:3000
```

## Deploy

```bash
# Start the container (detached)
docker-compose up -d
```

API is available at `http://localhost:8080/`

## Useful commands

```bash
# Stop and remove the container
docker-compose down

# View logs
docker-compose logs -f
```