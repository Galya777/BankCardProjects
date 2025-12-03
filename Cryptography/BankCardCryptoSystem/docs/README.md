# Bank Card Encryption System

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [System Architecture](#system-architecture)
4. [Installation](#installation)
5. [Usage](#usage)
6. [Security](#security)
7. [Troubleshooting](#troubleshooting)

## Overview
The Bank Card Encryption System provides secure encryption and decryption of credit card numbers using a substitution cipher. The system supports multiple clients connecting to either a multi-threaded server or an RMI server implementation.

## Features
- **Dual Server Implementation**: Choose between multi-threaded socket-based or RMI-based server
- **Secure Authentication**: Role-based access control with user credentials
- **Card Validation**: Implements Luhn algorithm for card number validation
- **Encryption**: Substitution cipher with configurable shift value
- **Audit Logging**: Tracks all encryption/decryption operations
- **User Management**: Admin interface for managing users and permissions

## System Architecture

### Components
1. **Common Module**
   - Shared data models (User, CardData)
   - Encryption/decryption utilities
   - Card validation logic

2. **Multi-threaded Server**
   - Handles concurrent client connections
   - Thread pool for efficient resource management
   - JavaFX admin interface

3. **RMI Server**
   - Remote method invocation interface
   - Centralized service implementation
   - JavaFX admin interface

4. **Client Application**
   - JavaFX-based user interface
   - Support for both server implementations
   - Input validation and error handling

## Installation

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Build Instructions
```bash
# Clone the repository
git clone <repository-url>

# Build the project
mvn clean install
```

## Usage

### Starting the Server
```bash
# For multi-threaded server
java -jar multithreaded-server/target/multithreaded-server-1.0.jar

# For RMI server
java -jar rmi-server/target/rmi-server-1.0.jar
```

### Starting the Client
```bash
java -jar client/target/client-1.0.jar
```

## Security
- All passwords are hashed before storage
- Input validation on both client and server
- Role-based access control
- Secure session management

## Troubleshooting
### Common Issues
1. **Connection Refused**
   - Verify server is running
   - Check firewall settings
   - Ensure correct port numbers

2. **Authentication Failures**
   - Verify username/password
   - Check user permissions
   - Ensure account is not locked

3. **Card Validation Errors**
   - Check card number format
   - Verify Luhn algorithm compliance
   - Ensure proper card type prefix (3,4,5,6)
