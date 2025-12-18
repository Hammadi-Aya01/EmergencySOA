# EmergencySOA - Service-Oriented Architecture for Emergency Response Systems

## 📋 Project Overview

EmergencySOA is a robust Service-Oriented Architecture (SOA) implementation designed for emergency response and management systems. This project demonstrates modern enterprise integration patterns, microservices communication, and distributed system design principles for critical infrastructure applications.

**Project Status**: 🟢 Active | **Version**: 1.0.0 | **Last Updated**: December 2025

## ✨ Key Features

- **Distributed Service Architecture**: Modular SOA design with clearly separated service boundaries
- **Multi-Protocol Communication**: Supports various service communication patterns
- **Emergency Response Workflows**: Pre-configured workflows for common emergency scenarios
- **Configuration Management**: Centralized configuration with environment-specific profiles
- **Build Automation**: Maven-based build system with standardized project structure

## 🏗️ Architecture

### Technology Stack
- **Backend**: Java (97.6%)
- **Frontend/UI**: CSS (2.0%)
- **Automation**: Batchfile (0.4%)
- **Build Tool**: Apache Maven
- **Project Structure**: Standard Maven directory layout

### Project Structure
EmergencySOA/
├── src/main/ # Main source code
├── target/ # Build artifacts
├── .vscode/ # IDE configuration
├── pom.xml # Maven configuration
├── start-all.bat # Batch automation script
└── .gitignore # Version control exclusions

## 🚀 Getting Started

### Prerequisites
- Java JDK 8 or higher
- Apache Maven 3.6+
- Git

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Hammadi-Aya01/EmergencySOA.git
   cd EmergencySOA
2. Build the project : 
mvn clean install
3. Run the application: 
Terminal 1 (SOAP Server): 
mvn clean compile exec:java -P soap-server
Terminal 2 (REST Server):
mvn clean compile exec:java -P rest-server
Terminal 3 (GUI Application):
mvn clean javafx:run -P gui-app
🔧 Configuration
The project includes pre-configured settings in the .vscode directory for optimal development experience. Key configuration files:

pom.xml: Maven dependencies and build configuration

start-all.bat: Batch script for Windows environment startup

.gitignore: Proper exclusion of build artifacts and IDE files

📁 Repository Details
Commits: 4 total commits

Initial Release: December 17, 2025

Languages: Java (97.6%), CSS (2.0%), Batchfile (0.4%)

Project State: Complete with fixed configuration

🤝 Contributing
Contributions are welcome! Please follow these steps:

Fork the repository

Create a feature branch (git checkout -b feature/AmazingFeature)

Commit your changes (git commit -m 'Add some AmazingFeature')

Push to the branch (git push origin feature/AmazingFeature)

Open a Pull Request

📄 License
This project is currently unlicensed. For usage rights, please contact the repository owner.

🛠️ Development
IDE Setup
This project includes Visual Studio Code configuration files for immediate development setup. The workspace settings are optimized for Java development with Maven.

Build Commands : 
# Compile the project
mvn compile

# Run tests
mvn test

# Create executable JAR
mvn package

# Clean build artifacts
mvn clean
📈 Project Health
✅ Complete project structure

✅ Build configuration (Maven)

✅ IDE configuration

✅ Automation scripts

✅ Proper .gitignore setup

🔄 Documentation (in progress)

📞 Contact & Support
For questions, issues, or collaboration opportunities:

Mail: HammadiAya2004@gmail.com

Maintainer: Hammadi-Aya01
