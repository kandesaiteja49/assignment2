# Design Decisions

This document explains the architectural and design decisions made in the MediTrack application.

## Table of Contents
1. [Architectural Overview](#architectural-overview)
2. [Design Patterns](#design-patterns)
3. [Technology Choices](#technology-choices)
4. [Database Design](#database-design)
5. [AI Integration](#ai-integration)
6. [Security Considerations](#security-considerations)
7. [Scalability Design](#scalability-design)

## Architectural Overview

### Layered Architecture

We adopted a **layered architecture** pattern for clear separation of concerns:

```
Presentation Layer (Controllers)
         ↓
Service Layer (Business Logic)
         ↓
Repository Layer (Data Access)
         ↓
Database Layer (PostgreSQL)
```

**Rationale**:
- **Maintainability**: Each layer has a single responsibility
- **Testability**: Layers can be tested independently
- **Flexibility**: Changes in one layer don't cascade to others
- **Reusability**: Business logic can be reused across different controllers

### Package Structure

```
com.airtribe.meditrack/
├── config/          # Configuration classes
├── controller/      # REST endpoints
├── dto/            # Data Transfer Objects
├── entities/       # JPA entities
├── enums/          # Enumeration types
├── exceptions/     # Custom exceptions
├── factory/        # Factory pattern implementations
├── notificationService/ # Observer pattern
├── repositories/   # Data access layer
└── services/       # Business logic layer
    └── paymentservices/ # Strategy pattern
```

**Rationale**:
- Follows Spring Boot best practices
- Clear package naming reflects functionality
- Easy navigation for new developers
- Supports modular development

## Design Patterns

### 1. Observer Pattern (Notification System)

**Problem**: Need to notify multiple parties (patient, doctor, admin) when appointment status changes without tight coupling.

**Solution**: Observer pattern with `NotifySubject` and `Observer` interfaces.

**Implementation**:
```java
public interface Observer {
    void updateAppointment(Appointment appointment);
    void updateDoctorMessage(String message);
}

public interface NotifySubject {
    void notifyObserversPrivate(Appointment appointment);
    void notifyAll(String message);
}
```

**Benefits**:
- **Loose Coupling**: Subject doesn't need to know concrete observer classes
- **Extensibility**: Easy to add new observers (SMS, Email, Push notifications)
- **Single Responsibility**: Each observer handles its own notification logic
- **Open/Closed Principle**: Open for extension, closed for modification

**Alternative Considered**: Spring Events
- **Why not chosen**: Observer pattern provides more explicit control and is easier to understand for learning purposes

### 2. Strategy Pattern (Payment Processing)

**Problem**: Support multiple payment methods (UPI, Card, Cash) with different processing logic.

**Solution**: Strategy pattern with `PaymentStrategy` interface.

**Implementation**:
```java
public interface PaymentStrategy {
    Boolean processPayment(double amount);
}

public class UpiPayment implements PaymentStrategy { ... }
public class CardPayment implements PaymentStrategy { ... }
```

**Benefits**:
- **Runtime Flexibility**: Payment method can be selected at runtime
- **Easy Extension**: New payment methods can be added without modifying existing code
- **Testing**: Each strategy can be tested independently
- **Maintainability**: Payment logic is encapsulated in separate classes

**Alternative Considered**: Switch-case in service
- **Why not chosen**: Violates Open/Closed Principle and Single Responsibility Principle

### 3. Factory Pattern (Bill Generation)

**Problem**: Create different types of bills (Consultation, Follow-up, Lab Test) with varying tax rates and pricing rules.

**Solution**: Factory pattern with `BillFactory` class.

**Implementation**:
```java
public class BillFactory {
    public static Bill createBill(BillType billType, double amount) {
        return switch (billType) {
            case CONSULTATION -> new ConsultationBill(amount);
            case FOLLOW_UP -> new FollowUpBill(amount);
            case LAB_TEST -> new LabTestBill(amount);
        };
    }
}
```

**Benefits**:
- **Centralized Creation**: All bill creation logic in one place
- **Type Safety**: Compile-time checking with enums
- **Extensibility**: Easy to add new bill types
- **Encapsulation**: Bill creation details hidden from clients

**Alternative Considered**: Direct instantiation
- **Why not chosen**: Would violate DRY principle and make bill type changes difficult

### 4. Repository Pattern (Data Access)

**Problem**: Need abstraction over data access to support multiple data sources and improve testability.

**Solution**: Spring Data JPA Repository pattern.

**Implementation**:
```java
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialistAndIsAvailableTrue(Specialist specialist);
    
    @Query(value = "SELECT * FROM doctor d ORDER BY d.embedding <-> :embedding LIMIT :limit", 
           nativeQuery = true)
    List<Doctor> findSimilarDoctors(@Param("embedding") float[] embedding, 
                                   @Param("limit") int limit);
}
```

**Benefits**:
- **Abstraction**: Business logic doesn't depend on persistence details
- **CRUD for Free**: JpaRepository provides basic operations
- **Query Methods**: Method naming convention generates queries
- **Custom Queries**: Support for complex queries when needed
- **Testing**: Easy to mock for unit tests

**Alternative Considered**: Direct JDBC
- **Why not chosen**: Too low-level, more boilerplate code, harder to maintain

### 5. DTO Pattern (Data Transfer)

**Problem**: Prevent exposure of internal entity structure and control what data is sent to clients.

**Solution**: Data Transfer Objects (DTOs) with ModelMapper.

**Implementation**:
```java
public class DoctorDTO {
    private Long id;
    private String name;
    private Specialist specialist;
    // No sensitive information like embedding vectors
}

// Service layer
return modelMapper.map(doctor, DoctorDTO.class);
```

**Benefits**:
- **Security**: Hide sensitive fields
- **Performance**: Send only necessary data
- **Versioning**: Different DTOs for different API versions
- **Decoupling**: API changes don't affect internal entities

**Alternative Considered**: Return entities directly
- **Why not chosen**: Exposes internal structure, can cause circular JSON serialization, security risks

### 6. Builder Pattern (Object Construction)

**Problem**: Complex object creation with many optional parameters.

**Solution**: Builder pattern via Lombok's `@Builder` annotation.

**Implementation**:
```java
@Entity
@Builder
public class Appointment {
    private Doctor doctor;
    private Patient patient;
    private LocalDate startDate;
    // ... many fields
}

// Usage
Appointment appointment = Appointment.builder()
    .doctor(doctor)
    .patient(patient)
    .startDate(date)
    .status(AppointmentStatus.PENDING)
    .build();
```

**Benefits**:
- **Readability**: Clear what each parameter represents
- **Flexibility**: Only set needed parameters
- **Immutability**: Can create immutable objects
- **Validation**: Can add validation in builder

**Alternative Considered**: Telescoping constructors
- **Why not chosen**: Hard to read with many parameters, inflexible

## Technology Choices

### Spring Boot 3.3.5

**Why Spring Boot?**
- Industry-standard framework
- Convention over configuration
- Rich ecosystem
- Excellent documentation
- Built-in features (security, data access, web)

**Why version 3.3.5?**
- Stable LTS version
- Java 21 support
- Spring AI compatibility
- Modern features (virtual threads, records)

### Java 21

**Why Java 21?**
- Latest LTS version
- Virtual threads for better concurrency
- Pattern matching for better code readability
- Record classes for DTOs
- Improved performance

**Example of modern Java features used**:
```java
// Pattern matching in switch
return switch (billType) {
    case CONSULTATION -> new ConsultationBill(amount);
    case FOLLOW_UP -> new FollowUpBill(amount);
    case LAB_TEST -> new LabTestBill(amount);
};
```

### PostgreSQL with pgvector

**Why PostgreSQL?**
- Mature, reliable RDBMS
- ACID compliance
- Rich feature set
- Great performance

**Why pgvector?**
- Native vector similarity search
- Faster than application-level computation
- Efficient indexing (IVFFlat, HNSW)
- Seamless integration with Hibernate

**Alternative Considered**: 
- **MongoDB with vector search**: Less mature, weaker transactional support
- **Pinecone/Weaviate**: Additional service to manage, cost

### Spring AI with Ollama

**Why Spring AI?**
- Official Spring support
- Unified API for multiple LLM providers
- Integration with Spring ecosystem
- Easy to switch providers

**Why Ollama?**
- **Privacy**: Runs locally, no data leaves system
- **Cost**: Free, no API costs
- **Performance**: Fast inference on consumer hardware
- **Offline**: Works without internet
- **Development**: Perfect for development and testing

**Ollama Models Chosen**:
1. **phi3:mini** (2.3 GB)
   - Lightweight yet capable
   - Good for classification tasks
   - Fast inference
   
2. **nomic-embed-text:latest** (274 MB)
   - Optimized for embeddings
   - 768-dimensional vectors
   - Good semantic understanding

**Alternative Considered**:
- **OpenAI API**: Cost per request, requires internet, privacy concerns
- **Groq**: Fast but requires API key, external dependency

### Gradle

**Why Gradle over Maven?**
- More flexible and concise
- Better performance (incremental builds)
- Groovy/Kotlin DSL
- Better dependency management
- Modern tool

### Lombok

**Why Lombok?**
- Reduces boilerplate code
- Cleaner classes
- Less maintenance

**Features Used**:
- `@Data`: Getters, setters, toString, equals, hashCode
- `@Builder`: Builder pattern
- `@RequiredArgsConstructor`: Constructor for final fields
- `@SuperBuilder`: Builder for inheritance hierarchies

**Trade-off**: IDE plugin required, but worth it for code clarity

## Database Design

### Inheritance Strategy: JOINED

**Decision**: Use `@Inheritance(strategy = InheritanceType.JOINED)` for Person entity.

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {
    // Common fields
}

@Entity
public class Doctor extends Person {
    // Doctor-specific fields
}

@Entity
public class Patient extends Person {
    // Patient-specific fields
}
```

**Tables Created**:
- `person` (id, email, phone, address, role, timestamps)
- `doctor` (id → person.id, name, specialist, fee, embedding)
- `patient` (id → person.id, name, medical_history)

**Rationale**:
- **Normalization**: No data duplication
- **Referential Integrity**: Foreign key constraints
- **Query Flexibility**: Can query all persons or specific types
- **Clean Schema**: Each table contains only relevant fields

**Alternatives Considered**:

1. **SINGLE_TABLE**
   - Pros: Faster queries (no joins), simpler
   - Cons: Many nullable columns, wastes space, less normalized
   
2. **TABLE_PER_CLASS**
   - Pros: No joins, complete tables
   - Cons: Data duplication, complex polymorphic queries, no parent table

3. **@MappedSuperclass**
   - Pros: No person table
   - Cons: Can't query all persons, no polymorphism

### Vector Storage

**Decision**: Store embeddings as `float[]` using Hibernate Vector.

```java
@Column(columnDefinition = "vector(768)")
@JdbcTypeCode(SqlTypes.VECTOR)
private float[] embedding;
```

**Rationale**:
- Native PostgreSQL vector type
- Efficient similarity search with pgvector operators
- Indexed search with IVFFlat or HNSW
- No serialization overhead

**Query Example**:
```java
@Query("SELECT d FROM Doctor d ORDER BY d.embedding <-> :queryEmbedding")
List<Doctor> findSimilar(@Param("queryEmbedding") float[] embedding);
```

### Relationship Mappings

**One-to-Many: Doctor ↔ Appointments**
```java
@OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, 
           cascade = CascadeType.ALL, orphanRemoval = true)
private List<Appointment> appointments;
```

**Rationale**:
- **LAZY loading**: Appointments loaded only when needed
- **CASCADE.ALL**: Deleting doctor deletes appointments
- **orphanRemoval**: Removing appointment from list deletes it
- **mappedBy**: Bidirectional relationship with Appointment

## AI Integration

### Semantic Doctor Search

**Architecture**:
1. Doctor descriptions converted to embeddings during registration
2. User query converted to embedding
3. Cosine similarity search in PostgreSQL
4. Top-k similar doctors returned

**Implementation Details**:

```java
// 1. Generate embedding for doctor description
float[] embedding = embeddingService.getEmbedding(doctor.getDescription());
doctor.setEmbedding(embedding);

// 2. Search by user symptoms
float[] queryEmbedding = embeddingService.getEmbedding(symptoms);
List<Doctor> doctors = doctorRepo.findSimilarDoctors(queryEmbedding, limit);

// 3. PostgreSQL vector search
SELECT * FROM doctor 
ORDER BY embedding <-> '[0.1, 0.2, ..., 0.8]' 
LIMIT 5;
```

**Why This Approach?**
- **Semantic Understanding**: Matches meaning, not just keywords
- **Flexibility**: Works with natural language queries
- **Performance**: Database-level search is fast
- **Accuracy**: Better than keyword matching

### Smart Appointment Suggestions

**Architecture**:
1. LLM classifies symptoms to specialist type
2. Find available doctors of that specialty
3. Generate available time slots
4. Return recommendations

**Prompt Engineering**:
```
You are a medical triage classifier.
Return ONLY ONE WORD.
Return ONLY the ENUM value exactly as written.
...
```

**Rationale**:
- **Deterministic**: Temperature=0 for consistent results
- **Structured Output**: Returns enum directly, no parsing needed
- **Fast**: Small model (phi3:mini) for quick classification
- **Offline**: Works without internet

## Security Considerations

### Current Implementation

**Input Validation**:
```java
@Email(message = "Email should be valid")
private String email;

@Column(nullable = false)
private String name;
```

**Exception Handling**:
- Global exception handler for consistent error responses
- Custom exceptions for domain errors
- No stack traces exposed to clients

**SQL Injection Prevention**:
- JPA/Hibernate parameterized queries
- No string concatenation in queries

### Future Enhancements

1. **Authentication**: Spring Security with JWT
2. **Authorization**: Role-based access control (RBAC)
3. **Rate Limiting**: Prevent abuse of AI endpoints
4. **Data Encryption**: Encrypt sensitive fields (medical history)
5. **Audit Logging**: Track who accessed what data
6. **HTTPS**: Secure transport layer

## Scalability Design

### Current Architecture

**Stateless Services**:
- No session state in services
- Easy to scale horizontally

**Database Connection Pooling**:
- HikariCP (Spring Boot default)
- Configurable pool size

**Lazy Loading**:
- Entities loaded only when needed
- Reduces memory footprint

### Future Scalability

1. **Caching**:
   - Redis for frequently accessed data
   - Doctor list, specialties
   - Reduce database load

2. **Read Replicas**:
   - PostgreSQL read replicas for queries
   - Write to master, read from replicas

3. **Async Processing**:
   - Queue-based notification system
   - Background embedding generation
   - Batch processing for analytics

4. **Microservices** (if needed):
   - Doctor Service
   - Appointment Service
   - Payment Service
   - Notification Service

5. **Load Balancing**:
   - Multiple application instances
   - Nginx/HAProxy

## Performance Optimizations

### Implemented

1. **DTO Pattern**: Send only necessary data
2. **Lazy Loading**: Load related entities on demand
3. **Database Indexing**: 
   - Primary keys
   - Foreign keys
   - Email unique constraint
   - Vector indexes (IVFFlat)

4. **Connection Pooling**: HikariCP
5. **Query Optimization**: 
   - Select only needed columns
   - Use native queries for complex operations

### Monitoring

**Actuator Endpoints**:
```
/actuator/health - Application health
/actuator/metrics - Performance metrics
/actuator/info - Application info
```

## Trade-offs and Limitations

### Trade-offs Made

1. **JOINED Inheritance vs SINGLE_TABLE**
   - Chose: Better normalization
   - Cost: Join overhead

2. **Local LLM vs Cloud API**
   - Chose: Privacy and cost
   - Cost: Limited model capabilities

3. **Synchronous Notifications vs Async**
   - Chose: Simplicity
   - Cost: Blocking operations

4. **In-Memory Observers vs Persistent Queue**
   - Chose: Simplicity
   - Cost: Lost notifications on restart

### Known Limitations

1. **No Authentication**: Planned for future
2. **Simple Notification**: Console only, no email/SMS
3. **No Caching**: Every request hits database
4. **Single Database**: No replication
5. **No Rate Limiting**: AI endpoints can be abused

### Future Improvements

1. Add Spring Security
2. Implement Redis caching
3. Add email/SMS notifications
4. Deploy to cloud (AWS/Azure)
5. Add monitoring (Prometheus/Grafana)
6. Implement CI/CD pipeline
7. Add comprehensive integration tests
8. Create admin dashboard
9. Add real-time chat (WebSocket)
10. Implement appointment reminders

## Conclusion

The MediTrack application demonstrates:
- **Solid OOP principles** (SOLID)
- **Professional design patterns** (Observer, Strategy, Factory, Repository)
- **Modern technology stack** (Java 21, Spring Boot 3, PostgreSQL, AI)
- **Clean architecture** (Layered, separation of concerns)
- **Extensibility** (Easy to add features)
- **Maintainability** (Clear code, documentation)

These decisions balance **simplicity**, **scalability**, **performance**, and **learning value**.

---

**Last Updated**: February 2024
