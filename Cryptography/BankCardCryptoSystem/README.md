# Bank Card Cryptography System

A secure system for bank card encryption and decryption using Java RMI (Remote Method Invocation) with a JavaFX client interface.

## Features

- Secure user authentication
- Bank card number encryption/decryption
- Session management
- Modern JavaFX user interface
- Multi-threaded server architecture
- Input validation and error handling

## Prerequisites

- Java Development Kit (JDK) 11 or later
- Maven 3.6.0 or later
- Git (optional, for cloning the repository)

## Installation

1. **Clone the repository** (if not already done):
   ```bash
   git clone https://github.com/Galya777/BankCardProjects.git
   cd BankCardProjects/Cryptography/BankCardCryptoSystem
   ```

2. **Build the project** using Maven:
   ```bash
   mvn clean install
   ```

## Running the Application

### 1. Start the RMI Server

Open a terminal and run:

```bash
cd target/classes
java rmi.server.RMIServerImpl
```

The server will start and display a message indicating it's running on port 1099.

### 2. Start the Client Application

Open another terminal and run:

```bash
cd target/classes
java client.gui.BankCardApp
```

### Default Credentials
- **Username:** `admin`
- **Password:** `admin123`

## Project Structure

```
BankCardCryptoSystem/
├── client/               # Client-side code
│   ├── client/           # RMI client implementation
│   └── gui/              # JavaFX UI components
├── common/               # Shared code between client and server
│   ├── model/            # Data models
│   └── util/             # Utility classes
├── rmi-server/           # RMI server implementation
│   └── server/           # Server-side code
├── multithreaded-server/ # Multi-threaded server (alternative implementation)
└── src/
    └── main/
        └── resources/    # Application resources
```

## How to Use

1. **Login** using the provided credentials
2. **Encrypt a card**:
   - Enter a valid card number
   - Set a shift value (1-9)
   - Click "Encrypt"
3. **Decrypt a card**:
   - Enter an encrypted card number
   - Enter the correct shift value
   - Click "Decrypt"
4. **View History**:
   - All encrypted cards are stored and can be viewed in the history section

## Security Notes

- Passwords are hashed using PBKDF2 with SHA-256
- Session timeouts are enforced after 30 minutes of inactivity
- Card numbers are validated using Luhn algorithm
- Input validation is performed on both client and server sides

## Troubleshooting

- **RMI Connection Issues**:
  - Ensure the RMI server is running before starting the client
  - Check if port 1099 is available and not blocked by a firewall

- **Java Version**:
  - Ensure you're using Java 11 or later
  - Set the correct JAVA_HOME environment variable

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
