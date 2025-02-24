# Banking Solution

A simple REST API for a banking application supporting basic operations like account creation, deposits, withdrawals, and fund transfers.

## Features

- **Account Management:**
  - Create new accounts (initial balance set to zero)
  - Retrieve account details by account number
  - List all accounts for a user

- **Account Transactions:**
  - Deposit funds
  - Withdraw funds
  - Transfer funds between accounts

- **Technology Stack:**
  - **Java** & **Spring Boot**
  - **PostgreSQL**
  - **JWT** for authentication
  - **JUnit** & **Mockito** for testing
  - **Gradle** for build and code coverage (JaCoCo)
  - **Docker Compose** for local development

## Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Repinskie/CopyWise.git
   cd CopyWise

## Environment Variables

Default environment variables are provided for ease of testing. Adjust as needed in your local environment or in the `docker-compose.yml`.

## Build the Application

```bash
docker-compose up --build 
```

## You can test the application via Postman at:
```bash
http://localhost:8091/copywise
```
