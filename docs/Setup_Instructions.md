# Setup Instructions

This document provides comprehensive setup instructions for the MediTrack application.

## Table of Contents
1. [System Requirements](#system-requirements)
2. [Installing Prerequisites](#installing-prerequisites)
3. [Database Setup](#database-setup)
4. [Ollama Setup](#ollama-setup)
5. [Application Configuration](#application-configuration)
6. [Building and Running](#building-and-running)
7. [Troubleshooting](#troubleshooting)

## System Requirements

### Minimum Requirements
- **CPU**: Dual-core processor (2.0 GHz or higher)
- **RAM**: 8 GB (16 GB recommended for AI features)
- **Storage**: 10 GB free space
- **OS**: Windows 10+, macOS 10.15+, or Linux (Ubuntu 20.04+)

### Software Requirements
- Java Development Kit (JDK) 21
- PostgreSQL 14 or higher
- Ollama (for AI features)
- Git
- Gradle 8.x (optional, wrapper included)

## Installing Prerequisites

### 1. Install Java 21

#### On Ubuntu/Debian
```bash
# Add repository
sudo apt update
sudo apt install -y wget apt-transport-https

# Install OpenJDK 21
sudo apt install openjdk-21-jdk

# Verify installation
java -version
```

#### On macOS
```bash
# Using Homebrew
brew install openjdk@21

# Add to PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verify installation
java -version
```

#### On Windows
1. Download JDK 21 from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/)
2. Run the installer
3. Set JAVA_HOME environment variable
4. Add `%JAVA_HOME%\bin` to PATH
5. Verify: `java -version`

### 2. Install PostgreSQL

#### On Ubuntu/Debian
```bash
# Install PostgreSQL
sudo apt update
sudo apt install postgresql postgresql-contrib

# Start PostgreSQL service
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Verify installation
psql --version
```

#### On macOS
```bash
# Using Homebrew
brew install postgresql@14

# Start PostgreSQL
brew services start postgresql@14

# Verify installation
psql --version
```

#### On Windows
1. Download from [PostgreSQL official site](https://www.postgresql.org/download/windows/)
2. Run the installer
3. Remember the superuser password you set
4. Verify: `psql --version`

### 3. Install pgvector Extension

```bash
# On Ubuntu/Debian
sudo apt install postgresql-14-pgvector

# On macOS
brew install pgvector

# On Windows
# Download from https://github.com/pgvector/pgvector/releases
# Follow installation instructions for Windows
```

### 4. Install Git

#### On Ubuntu/Debian
```bash
sudo apt install git
```

#### On macOS
```bash
brew install git
```

#### On Windows
Download from [git-scm.com](https://git-scm.com/download/win)

## Database Setup

### Step 1: Access PostgreSQL

```bash
# On Linux/macOS
sudo -u postgres psql

# On Windows (as administrator)
psql -U postgres
```

### Step 2: Create Database

```sql
-- Create database
CREATE DATABASE meditrack;

-- Connect to database
\c meditrack;

-- Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- Create user (optional)
CREATE USER meditrack_user WITH PASSWORD 'your_secure_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE meditrack TO meditrack_user;

-- Exit
\q
```

### Step 3: Configure Database Port (if needed)

If you're using a different port (e.g., 5433), update PostgreSQL configuration:

```bash
# Edit postgresql.conf
sudo nano /etc/postgresql/14/main/postgresql.conf

# Find and modify:
port = 5433

# Restart PostgreSQL
sudo systemctl restart postgresql
```

### Step 4: Verify Database Setup

```bash
# Test connection
psql -h localhost -p 5433 -U postgres -d meditrack

# Verify pgvector extension
\dx

# Expected output should include:
# vector | 0.x.x | public | vector data type...

# Exit
\q
```

## Ollama Setup

### Step 1: Install Ollama

#### On Linux
```bash
curl -fsSL https://ollama.com/install.sh | sh
```

#### On macOS
```bash
brew install ollama
```

#### On Windows
Download from [ollama.ai](https://ollama.ai/download)

### Step 2: Start Ollama Service

```bash
# On Linux/macOS
ollama serve

# On Windows - Ollama starts automatically
```

### Step 3: Download Required Models

Open a new terminal and run:

```bash
# Download chat model (phi3:mini - ~2.3 GB)
ollama pull phi3:mini

# Download embedding model (nomic-embed-text - ~274 MB)
ollama pull nomic-embed-text:latest

# Verify models
ollama list
```

Expected output:
```
NAME                        SIZE
phi3:mini                  2.3 GB
nomic-embed-text:latest    274 MB
```

### Step 4: Test Ollama

```bash
# Test chat model
ollama run phi3:mini "Hello, how are you?"

# Test embedding generation
curl http://localhost:11434/api/embeddings \
  -d '{"model": "nomic-embed-text:latest", "prompt": "test"}'
```

## Application Configuration

### Step 1: Clone Repository

```bash
git clone <repository-url>
cd medi-track-feature-patient-doctor-registration
```

### Step 2: Configure application.properties

Edit `src/main/resources/application.properties`:

```properties
# Application Configuration
spring.application.name=meditrack
server.port=8081

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/meditrack
spring.datasource.username=postgres
spring.datasource.password=postgres

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# Spring AI Configuration
spring.ai.chat.client.enabled=false

# Ollama Configuration
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=phi3:mini
spring.ai.ollama.embedding.model=nomic-embed-text:latest

# Optional: Groq API (if using cloud-based models)
# spring.ai.openai.api-key=your-api-key-here
# spring.ai.openai.base-url=https://api.groq.com/openai
# spring.ai.openai.chat.options.model=openai/gpt-oss-120b
```

### Important Configuration Notes

1. **Database URL**: Adjust port (5433 vs 5432) based on your PostgreSQL setup
2. **Hibernate DDL**: 
   - `create-drop` - Recreates schema on each start (development)
   - `update` - Updates schema without dropping (production)
   - `validate` - Only validates schema (production)
3. **Ollama URL**: Ensure Ollama is running on port 11434

## Building and Running

### Step 1: Make Gradle Wrapper Executable

```bash
chmod +x gradlew
```

### Step 2: Build the Project

```bash
# Clean and build
./gradlew clean build

# Skip tests (if needed)
./gradlew clean build -x test
```

### Step 3: Run the Application

#### Method 1: Using Gradle
```bash
./gradlew bootRun
```

#### Method 2: Using JAR
```bash
# Build JAR
./gradlew build

# Run JAR
java -jar build/libs/meditrack-0.0.1-SNAPSHOT.jar
```

#### Method 3: Using IDE
1. Import project into IntelliJ IDEA / Eclipse
2. Run `MeditrackApplication.java`

### Step 4: Verify Application

```bash
# Check health
curl http://localhost:8081/actuator/health

# Expected: {"status":"UP"}

# Check Ollama health
curl http://localhost:8081/ollama/health

# Test embedding service
curl http://localhost:8081/ollama/test
```

## Troubleshooting

### Issue 1: Database Connection Failed

**Error**: `Connection to localhost:5433 refused`

**Solutions**:
```bash
# Check if PostgreSQL is running
sudo systemctl status postgresql

# Start PostgreSQL if not running
sudo systemctl start postgresql

# Verify port
sudo netstat -plnt | grep postgres

# Check logs
sudo tail -f /var/log/postgresql/postgresql-14-main.log
```

### Issue 2: pgvector Extension Not Found

**Error**: `ERROR: extension "vector" is not available`

**Solutions**:
```bash
# Install pgvector
sudo apt install postgresql-14-pgvector

# Verify installation
psql -d meditrack -c "CREATE EXTENSION IF NOT EXISTS vector;"
```

### Issue 3: Ollama Not Responding

**Error**: `Connection refused to http://localhost:11434`

**Solutions**:
```bash
# Check if Ollama is running
ps aux | grep ollama

# Start Ollama
ollama serve

# Check if models are downloaded
ollama list

# Test Ollama directly
curl http://localhost:11434/api/tags
```

### Issue 4: Java Version Mismatch

**Error**: `Unsupported class file major version 65`

**Solutions**:
```bash
# Check Java version
java -version

# Should be 21.x.x

# Set JAVA_HOME if multiple versions installed
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Verify
java -version
```

### Issue 5: Out of Memory Error

**Error**: `Java heap space`

**Solutions**:
```bash
# Increase JVM heap size
export JAVA_OPTS="-Xmx4g -Xms2g"

# Run application with more memory
./gradlew bootRun --args='--spring-boot.run.jvmArguments="-Xmx4g"'

# Or modify application.properties
# spring.jpa.properties.hibernate.jdbc.batch_size=20
```

### Issue 6: Port Already in Use

**Error**: `Port 8081 is already in use`

**Solutions**:
```bash
# Find process using port
lsof -i :8081
# or
netstat -tulpn | grep 8081

# Kill process (use PID from above)
kill -9 <PID>

# Or change port in application.properties
server.port=8082
```

### Issue 7: Gradle Build Failed

**Error**: `Could not resolve dependencies`

**Solutions**:
```bash
# Clear Gradle cache
./gradlew clean --refresh-dependencies

# Delete .gradle directory
rm -rf ~/.gradle/caches/

# Rebuild
./gradlew clean build
```

## Environment-Specific Configurations

### Development Environment

```properties
# application-dev.properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
logging.level.org.springframework=DEBUG
```

Run with:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Production Environment

```properties
# application-prod.properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.org.springframework=WARN
```

Run with:
```bash
java -jar meditrack.jar --spring.profiles.active=prod
```

## Next Steps

After successful setup:

1. Explore the API using the examples in [README.md](../README.md)
2. Load sample data using DataLoader
3. Test AI features with smart appointment suggestions
4. Review design decisions in [Design_Decisions.md](Design_Decisions.md)
5. Check performance metrics in [JVM_Report.md](JVM_Report.md)

## Support

If you encounter issues not covered here:

1. Check application logs: `logs/spring.log`
2. Review PostgreSQL logs: `/var/log/postgresql/`
3. Verify Ollama logs: `ollama logs`
4. Check GitHub Issues
5. Contact the development team

---

**Last Updated**: February 2024
