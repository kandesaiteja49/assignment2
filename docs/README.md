# MediTrack - Intelligent Healthcare Appointment Management System

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue.svg)](https://www.postgresql.org/)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0-green.svg)](https://spring.io/projects/spring-ai)

## 📋 Table of Contents
- [Overview](#overview)
- [Key Features](#key-features)
- [Design Patterns Implemented](#design-patterns-implemented)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Sample Usage](#sample-usage)
- [Architecture](#architecture)
- [Contributing](#contributing)

## 🎯 Overview

MediTrack is a comprehensive healthcare appointment management system that leverages AI to provide intelligent doctor recommendations based on patient symptoms. The system uses vector embeddings and semantic search to match patients with the most suitable specialists.

### What Makes It Special?
- **AI-Powered Doctor Matching**: Uses Ollama embeddings to understand symptoms and recommend appropriate specialists
- **Smart Appointment Scheduling**: Intelligent slot management with conflict detection
- **Real-time Notifications**: Observer pattern implementation for appointment updates
- **Flexible Payment Processing**: Multiple payment strategies (UPI, Card)
- **Advanced Billing System**: Factory pattern for different bill types
- **Semantic Search**: Vector similarity search for finding doctors based on symptom descriptions

## ✨ Key Features

### Core Functionality
- 👨‍⚕️ **Doctor Registration & Management**
- 🧑‍🤝‍🧑 **Patient Registration & Management**
- 📅 **Appointment Booking & Scheduling**
- 💰 **Payment Processing** (UPI, Card)
- 🧾 **Bill Generation** (Consultation, Follow-up, Lab Test)
- 🔔 **Real-time Notifications**
- 📊 **Doctor Analytics** (Appointment counts, availability)

### AI-Powered Features
- 🤖 **Symptom-Based Doctor Recommendation**
- 🔍 **Semantic Doctor Search** using vector embeddings
- 💬 **AI Chat Interface** for patient queries
- 📈 **Smart Slot Suggestions**

### Advanced Features
- ⏰ **Slot Management** with conflict detection
- 🔄 **Appointment Status Tracking** (Pending → Scheduled → Completed/Cancelled)
- 📱 **RESTful API** architecture
- 🗄️ **PostgreSQL with pgvector** extension
- 🎨 **Clean Architecture** with separation of concerns

## 🏗️ Design Patterns Implemented

This project demonstrates professional software engineering practices through the implementation of multiple design patterns:

### 1. **Observer Pattern** (Behavioral)
**Location**: `notificationService/`
- **Purpose**: Real-time notification system for appointment updates
- **Components**:
  - `Observer` interface - Defines update contract
  - `NotifySubject` interface - Subject that observers subscribe to
  - `PatientNotificationObserver` - Sends notifications to patients
  - `DoctorNotificationObserver` - Sends notifications to doctors
  - `AppointmentService` - Implements NotifySubject to notify observers

**Use Case**: When an appointment is booked/cancelled/completed, all registered observers (patient and doctor) are automatically notified.

```java
// Example notification flow
public void bookAppointment(...) {
    // ... booking logic
    appointmentRepo.save(appoint);
    notifyObserversPrivate(appoint); // Notifies all observers
}
```

### 2. **Strategy Pattern** (Behavioral)
**Location**: `services/paymentservices/`
- **Purpose**: Flexible payment processing with multiple payment methods
- **Components**:
  - `PaymentStrategy` interface - Defines payment contract
  - `UpiPayment` - UPI payment implementation
  - `CardPayment` - Card payment implementation
  - `PaymentService` - Context that uses strategy

**Use Case**: Users can choose different payment methods (UPI, Card) without changing the payment processing logic.

```java
// Dynamic payment strategy selection
Boolean paid = paymentService.processPayment(
    paymentDto.getPaymentType(), 
    paymentDto.getPaymentAmount()
);
```

### 3. **Factory Pattern** (Creational)
**Location**: `factory/BillFactory.java`
- **Purpose**: Centralized creation of different bill types
- **Components**:
  - `BillFactory` - Creates bills based on type
  - `Bill` - Abstract base class
  - `ConsultationBill`, `FollowUpBill`, `LabTestBill` - Concrete implementations

**Use Case**: Generate different types of bills (Consultation, Follow-up, Lab Test) with specific tax rates and pricing rules.

```java
Bill bill = BillFactory.createBill(
    BillType.CONSULTATION, 
    doctor.getConsultationFee()
);
```

### 4. **Repository Pattern** (Architectural)
**Location**: `repositories/`
- **Purpose**: Abstraction layer for data access
- **Components**: All `*Repo` interfaces extending JpaRepository
- **Benefits**:
  - Decouples business logic from persistence
  - Enables easier testing with mock repositories
  - Provides CRUD operations out-of-the-box

### 5. **DTO Pattern** (Architectural)
**Location**: `dto/`
- **Purpose**: Data transfer between layers
- **Benefits**:
  - Prevents over-fetching of data
  - Hides internal entity structure
  - Reduces coupling between layers

### 6. **Builder Pattern** (Creational)
**Usage**: Lombok's `@Builder` and `@SuperBuilder` annotations
- **Purpose**: Fluent object construction
- **Components**: Used extensively in entities (Doctor, Patient, Appointment)

```java
Appointment appoint = Appointment.builder()
    .doctor(doctor)
    .patient(patient)
    .startDate(appointment.getStartDate())
    .status(AppointmentStatus.PAYMENT_PENDING)
    .build();
```

### 7. **Dependency Injection Pattern** (Architectural)
**Framework**: Spring Framework's DI container
- **Purpose**: Loose coupling and testability
- **Implementation**: Constructor injection via `@RequiredArgsConstructor`

### 8. **Singleton Pattern** (Creational)
**Framework**: Spring's bean management
- **Purpose**: Single instance of services/repositories
- **Implementation**: All `@Service`, `@Repository`, `@Controller` beans

## 🛠️ Technology Stack

### Backend Framework
- **Java 21** - Latest LTS version with modern features
- **Spring Boot 3.3.5** - Application framework
- **Spring Data JPA** - ORM and data access
- **Spring AI 1.0.0** - AI integration framework

### AI & Machine Learning
- **Ollama** - Local LLM inference
  - Model: `phi3:mini` for chat
  - Model: `nomic-embed-text:latest` for embeddings
- **Spring AI Ollama** - Ollama integration
- **Hibernate Vector** - Vector data type support
- **pgvector** - PostgreSQL vector extension

### Database
- **PostgreSQL 14+** - Main database
- **pgvector extension** - Vector similarity search

### Additional Libraries
- **Lombok** - Boilerplate code reduction
- **ModelMapper** - Object mapping
- **Hibernate Validator** - Input validation

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

1. **Java Development Kit (JDK) 21**
   ```bash
   java -version
   # Should output: java version "21.x.x"
   ```

2. **PostgreSQL 14+** with pgvector extension
   ```bash
   psql --version
   # Should output: psql (PostgreSQL) 14.x or higher
   ```

3. **Ollama** (for AI features)
   ```bash
   ollama --version
   # Install from: https://ollama.ai/
   ```

4. **Gradle** (optional, wrapper included)
   ```bash
   gradle --version
   ```

5. **Git** (for cloning)
   ```bash
   git --version
   ```

## ⚙️ Setup Instructions

Detailed setup instructions are available in [`docs/Setup_Instructions.md`](docs/Setup_Instructions.md)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/Poorna-chandra2000/medi-track.git
   cd medi-track-feature-patient-doctor-registration
   https://github.com/kandesaiteja49/assignment2.git
   
   ```

2. **Setup PostgreSQL Database**
   ```bash
   # Create database
   createdb meditrack
   
   # Enable pgvector extension
   psql -d meditrack -c "CREATE EXTENSION IF NOT EXISTS vector;"
   ```

3. **Configure application.properties**
   ```properties
   # Update database credentials if needed
   spring.datasource.url=jdbc:postgresql://localhost:5433/meditrack
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

4. **Install Ollama Models**
   ```bash
   ollama pull phi3:mini
   ollama pull nomic-embed-text:latest
   ```

5. **Build the project**
   ```bash
   ./gradlew clean build
   ```

6. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8081`

## 🚀 Running the Application

### Using Gradle Wrapper (Recommended)
```bash
./gradlew bootRun
```

### Using JAR file
```bash
./gradlew build
java -jar build/libs/meditrack-0.0.1-SNAPSHOT.jar
```

### Verify Installation
```bash
# Check if application is running
curl http://localhost:8081/actuator/health

# Expected output:
# {"status":"UP"}
```

## 📡 API Endpoints

### Health Check
```bash
GET /actuator/health
```

### Doctor Management
```bash
# Register a doctor
POST /doctor/register
Content-Type: application/json

{
  "name": "Dr. John Smith",
  "email": "john.smith@hospital.com",
  "phone": "9876543210",
  "address": "Mumbai",
  "specialist": "CARDIOLOGIST",
  "consultationFee": 500,
  "description": "Experienced cardiologist specializing in heart disease..."
}

# Get all doctors
GET /doctor/all

# Get doctor by ID
GET /doctor/{id}

# Get doctors by specialty
GET /doctor/specialist/{specialist}

# Search doctors by symptoms (AI-powered)
GET /doctor/semantic-search?query=chest pain and shortness of breath
```

### Patient Management
```bash
# Register a patient
POST /patient/register
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane.doe@email.com",
  "phone": "9123456789",
  "address": "Delhi"
}

# Get all patients
GET /patient/all

# Get patient by ID
GET /patient/{id}
```

### Appointment Management
```bash
# Book an appointment
POST /appointment/book?docid=1&patid=1
Content-Type: application/json

{
  "startDate": "2024-02-15",
  "startTime": "10:00",
  "endTime": "10:30",
  "patientSymptoms": "Chest pain and breathing difficulty"
}

# Confirm appointment with payment
POST /appointment/confirm/{appointmentId}
Content-Type: application/json

{
  "paymentAmount": 500,
  "paymentType": "UPI",
  "billType": "CONSULTATION"
}

# Complete appointment (Doctor)
POST /appointment/complete/{appointmentId}
Content-Type: application/json

{
  "docObservations": "Patient diagnosed with mild hypertension. Prescribed medications."
}

# Cancel appointment
POST /appointment/cancel/{appointmentId}?reason=Patient unavailable

# Get appointment details
GET /appointment/{id}
```

### AI-Powered Smart Appointments
```bash
# Get doctor suggestions based on symptoms
GET /smart/suggest-slots?symptoms=severe headache and dizziness&date=2024-02-15

# Response includes:
# - Recommended specialist type
# - Available doctors
# - Available time slots
```

### Analytics
```bash
# Get doctor appointment counts
GET /doctor/appointment-counts
```

## 📖 Sample Usage

### Complete Workflow Example

#### 1. Register a Doctor
```bash
curl -X POST http://localhost:8081/doctor/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dr. Sarah Johnson",
    "email": "sarah.j@cardio.com",
    "phone": "9001234567",
    "address": "Mumbai",
    "specialist": "CARDIOLOGIST",
    "consultationFee": 800,
    "description": "Expert cardiologist specializing in heart disease, chest pain, irregular heartbeat, and cardiovascular conditions with 15 years of experience."
  }'
```

**Expected Output:**
```json
{
  "id": 1,
  "name": "Dr. Sarah Johnson",
  "email": "sarah.j@cardio.com",
  "specialist": "CARDIOLOGIST",
  "consultationFee": 800.0,
  "isAvailable": true
}
```

#### 2. Register a Patient
```bash
curl -X POST http://localhost:8081/patient/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@email.com",
    "phone": "9123456789",
    "address": "Mumbai"
  }'
```

**Expected Output:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@email.com",
  "phone": "9123456789",
  "address": "Mumbai"
}
```

#### 3. AI-Powered Doctor Search
```bash
curl -X GET "http://localhost:8081/doctor/semantic-search?query=I have chest pain and shortness of breath"
```

**Expected Output:**
```json
[
  {
    "id": 1,
    "name": "Dr. Sarah Johnson",
    "specialist": "CARDIOLOGIST",
    "consultationFee": 800.0,
    "description": "Expert cardiologist...",
    "similarity": 0.87
  }
]
```

#### 4. Get Smart Appointment Suggestions
```bash
curl -X GET "http://localhost:8081/smart/suggest-slots?symptoms=chest pain and palpitations&date=2024-02-15"
```

**Expected Output:**
```json
{
  "specialist": "CARDIOLOGIST",
  "doctors": [
    {
      "doctorId": 1,
      "doctorName": "Dr. Sarah Johnson",
      "specialist": "CARDIOLOGIST",
      "availableSlots": [
        "09:00",
        "09:30",
        "10:00",
        "10:30",
        "11:00"
      ]
    }
  ]
}
```

#### 5. Book an Appointment
```bash
curl -X POST "http://localhost:8081/appointment/book?docid=1&patid=1" \
  -H "Content-Type: application/json" \
  -d '{
    "startDate": "2024-02-15",
    "startTime": "10:00",
    "endTime": "10:30",
    "patientSymptoms": "Chest pain and irregular heartbeat"
  }'
```

**Expected Output:**
```json
{
  "id": 1,
  "doctorName": "Dr. Sarah Johnson",
  "patientName": "John Doe",
  "startDate": "2024-02-15",
  "startTime": "10:00",
  "endTime": "10:30",
  "status": "PAYMENT_PENDING",
  "paymentAmount": 800.0
}
```

#### 6. Confirm Appointment with Payment
```bash
curl -X POST http://localhost:8081/appointment/confirm/1 \
  -H "Content-Type: application/json" \
  -d '{
    "paymentAmount": 800,
    "paymentType": "UPI",
    "billType": "CONSULTATION"
  }'
```

**Expected Output:**
```json
"Appointment confirmed and payment processed successfully."
```

**Console Notifications (Observer Pattern in Action):**
```
[Patient Notification] Appointment booked for John Doe with Dr. Sarah Johnson on 2024-02-15 at 10:00
[Doctor Notification] New appointment scheduled with patient John Doe on 2024-02-15 at 10:00
[Patient Notification] Payment successful. Appointment confirmed!
[Doctor Notification] Appointment confirmed with John Doe
```

#### 7. Complete Consultation (Doctor Action)
```bash
curl -X POST http://localhost:8081/appointment/complete/1 \
  -H "Content-Type: application/json" \
  -d '{
    "docObservations": "Patient shows signs of mild hypertension. ECG normal. Prescribed beta-blockers and lifestyle modifications. Follow-up in 2 weeks."
  }'
```

**Expected Output:**
```json
"Doctor consultation completed successfully."
```

#### 8. View Appointment Details
```bash
curl -X GET http://localhost:8081/appointment/1
```

**Expected Output:**
```json
{
  "id": 1,
  "doctorName": "Dr. Sarah Johnson",
  "patientName": "John Doe",
  "startDate": "2024-02-15",
  "startTime": "10:00",
  "endTime": "10:30",
  "status": "COMPLETED",
  "patientSymptoms": "Chest pain and irregular heartbeat",
  "docObservations": "Patient shows signs of mild hypertension...",
  "paymentAmount": 800.0
}
```

### Additional Test Scenarios

#### Cancel an Appointment
```bash
curl -X POST "http://localhost:8081/appointment/cancel/1?reason=Patient emergency"
```

#### Get Doctor Statistics
```bash
curl -X GET http://localhost:8081/doctor/appointment-counts
```

**Expected Output:**
```json
[
  {
    "doctorId": 1,
    "doctorName": "Dr. Sarah Johnson",
    "totalAppointments": 5,
    "completedAppointments": 3,
    "cancelledAppointments": 1,
    "pendingAppointments": 1
  }
]
```

#### Search Doctors by Specialty
```bash
curl -X GET http://localhost:8081/doctor/specialist/CARDIOLOGIST
```

## 🏛️ Architecture

### Layered Architecture
```
┌─────────────────────────────────────────┐
│         Controllers (REST API)          │
│  - DoctorController                     │
│  - PatientController                    │
│  - AppointmentController                │
│  - SmartAppointmentController           │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│          Service Layer                  │
│  - DoctorService                        │
│  - PatientService                       │
│  - AppointmentService (Observer)        │
│  - PaymentService (Strategy Context)    │
│  - EmbeddingService                     │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│       Repository Layer (JPA)            │
│  - DoctorRepo                           │
│  - PatientRepo                          │
│  - AppointmentRepo                      │
│  - PaymentRepo                          │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│      Database (PostgreSQL + pgvector)   │
└─────────────────────────────────────────┘
```

### Key Architectural Decisions
See [`docs/Design_Decisions.md`](docs/Design_Decisions.md) for detailed explanations.

## 📚 Documentation

- **[Setup Instructions](docs/Setup_Instructions.md)** - Detailed environment setup
- **[Design Decisions](docs/Design_Decisions.md)** - Architecture and pattern choices
- **[JVM Report](docs/JVM_Report.md)** - Performance analysis and tuning

## 🧪 Testing

Run tests using:
```bash
./gradlew test
```

For manual testing, use the provided TestRunner:
```bash
./gradlew test --tests TestRunner
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/patient-doctor-registration)`)
3. Commit your changes (`git commit -m 'ffeat: Add patient-doctor registration feature'`)
4. Push to the branch (`git push origin feature/patient-doctor-registration)`)
5. Open a Pull Request

## 📄 License

This project is part of an educational assignment.

## 👥 Authors

- Poorna Chandra S
- Sai Teja 

## 🙏 Acknowledgments

- Spring Framework team for excellent documentation
- Anthropic for AI assistance
- PostgreSQL community for pgvector extension
- Ollama team for local LLM infrastructure

---

**Project Status**: Active Development

**Last Updated**: February 2024
