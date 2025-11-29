# Cloud Vendor API - Spring Boot CRUD Application

## Overview
This is a complete Spring Boot REST API application that demonstrates CRUD operations for managing Cloud Vendor information. The application uses Spring Boot, Spring Data JPA, and MySQL database.

## Technologies Used
- **Spring Boot 3.2.0** - Framework for rapid application development
- **Spring Data JPA** - Data access layer with JPA/Hibernate
- **MySQL** - Relational database for data persistence
- **Maven** - Dependency management and build tool
- **Java 17** - Programming language

## Features
- Complete CRUD operations (Create, Read, Update, Delete)
- RESTful API endpoints
- MySQL database integration
- Input validation
- Error handling
- Cross-origin support (CORS)

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/cloudvendor` | Create a new cloud vendor |
| GET | `/cloudvendor/{id}` | Get vendor by ID |
| GET | `/cloudvendor` | Get all vendors |
| PUT | `/cloudvendor` | Update existing vendor |
| DELETE | `/cloudvendor/{id}` | Delete vendor by ID |
| GET | `/cloudvendor/health` | Health check endpoint |

## Data Model
Cloud Vendor entity has the following properties:
- `vendorId` (String) - Primary key, unique identifier
- `vendorName` (String) - Name of the vendor
- `vendorAddress` (String) - Address of the vendor
- `vendorPhoneNumber` (String) - Phone number of the vendor

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## Database Setup
1. Install MySQL and create a database named `cloudvendor_db`
2. Update database credentials in `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/cloudvendor_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
       username: your_username
       password: your_password
   ```

## Running the Application

### Using Maven
```bash
mvn spring-boot:run
```

### Using Java
```bash
mvn clean package
java -jar target/cloud-vendor-api-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Testing with Postman

### 1. Create Vendor (POST)
```
URL: http://localhost:8080/cloudvendor
Method: POST
Content-Type: application/json

Body:
{
    "vendorId": "V001",
    "vendorName": "Amazon Web Services",
    "vendorAddress": "Seattle, WA, USA",
    "vendorPhoneNumber": "+1-206-266-1000"
}
```

### 2. Get All Vendors (GET)
```
URL: http://localhost:8080/cloudvendor
Method: GET
```

### 3. Get Vendor by ID (GET)
```
URL: http://localhost:8080/cloudvendor/V001
Method: GET
```

### 4. Update Vendor (PUT)
```
URL: http://localhost:8080/cloudvendor
Method: PUT
Content-Type: application/json

Body:
{
    "vendorId": "V001",
    "vendorName": "Amazon Web Services (Updated)",
    "vendorAddress": "Seattle, WA, USA",
    "vendorPhoneNumber": "+1-206-266-2000"
}
```

### 5. Delete Vendor (DELETE)
```
URL: http://localhost:8080/cloudvendor/V001
Method: DELETE
```

### 6. Health Check (GET)
```
URL: http://localhost:8080/cloudvendor/health
Method: GET
```

## Project Structure
```
src/
├── main/
│   ├── java/com/example/cloudvendor/
│   │   ├── controller/
│   │   │   └── CloudVendorController.java
│   │   ├── entity/
│   │   │   └── CloudVendor.java
│   │   ├── repository/
│   │   │   └── CloudVendorRepository.java
│   │   ├── service/
│   │   │   ├── CloudVendorService.java
│   │   │   └── impl/
│   │   │       └── CloudVendorServiceImpl.java
│   │   └── CloudVendorApiApplication.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/
```

## Sample Test Data
```json
[
    {
        "vendorId": "AWS001",
        "vendorName": "Amazon Web Services",
        "vendorAddress": "Seattle, WA, USA",
        "vendorPhoneNumber": "+1-206-266-1000"
    },
    {
        "vendorId": "GCP001",
        "vendorName": "Google Cloud Platform",
        "vendorAddress": "Mountain View, CA, USA",
        "vendorPhoneNumber": "+1-650-253-0000"
    },
    {
        "vendorId": "AZ001",
        "vendorName": "Microsoft Azure",
        "vendorAddress": "Redmond, WA, USA",
        "vendorPhoneNumber": "+1-425-882-8080"
    }
]
```

## Error Handling
The API provides proper HTTP status codes and error messages:
- `201 Created` - Successful creation
- `200 OK` - Successful retrieval/update/deletion
- `400 Bad Request` - Invalid input or vendor already exists
- `404 Not Found` - Vendor not found

## Logging
The application includes detailed logging configuration in `application.yml` for debugging and monitoring.

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License.
