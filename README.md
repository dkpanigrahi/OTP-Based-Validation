# Mobile Number/OTP-Based Authentication System

This project implements a backend system for mobile number and OTP-based authentication using **Spring Boot**. It includes functionalities for user registration, OTP generation and validation, and device fingerprint verification.

---

## Features

1. **User Registration**:
   - Users can register with a mobile number and a device fingerprint.
   
2. **Login with OTP**:
   - Registered users can log in using an OTP sent to their mobile number.

3. **Resend OTP**:
   - If an OTP expires, users can request a new OTP.

4. **Get User Details**:
   - Logged-in users can fetch their profile details.

5. **Device Fingerprint Validation**:
   - Verifies if a user is logging in from the same device to enhance security.

6. **Scalable Deployment**:
   - Configurable for containerized environments and cloud platforms.

---

## Technology Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL** (or any SQL-based database)
- **Maven** (Dependency Management)

---

## Prerequisites

1. **Install Java** (17 or later).
2. **Install MySQL** and create a database named `otp_auth_db`.
3. **Postman** or any API testing tool.

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd <repository-directory>

