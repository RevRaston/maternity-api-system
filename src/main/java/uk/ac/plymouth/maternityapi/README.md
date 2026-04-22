# COMP2005 Assessment 2 – Automated Software Testing

## Overview
This project is a Java Spring Boot web-service API developed for COMP2005 Assessment 2. It connects to the provided Maternity Unit Web Service API and exposes custom endpoints to satisfy the coursework requirements.

## Features
The API provides the following functionality:

- **F1** - A list of rooms that have been used by a specific patient
- **F2** - A list of patients who have been in a specific room within the last 7 days
- **F3** - Identify the least used room
- **F4** - A list of staff who are responsible for 3 or more patients concurrently

## External API
This application connects to the university-provided API:

`https://web.socem.plymouth.ac.uk/COMP2005/api`

## Project Structure
- `client` - Handles calls to the external API
- `model` - Java model classes for external API data
- `service` - Business logic for F1–F4
- `controller` - REST endpoints
- `config` - Spring configuration
- `src/test` - Unit and integration tests

## Endpoints
- `GET /api/patients/{patientId}/rooms`
- `GET /api/rooms/{roomNumber}/patients`
- `GET /api/rooms/least-used`
- `GET /api/staff/overloaded`

## Development/Test Endpoints
The following temporary endpoints were used during development to inspect source API data:
- `GET /test/admissions`
- `GET /test/room-allocations`
- `GET /test/patients`
- `GET /test/allocations`
- `GET /test/employees`

## How to Run
1. Open the project in IntelliJ IDEA
2. Ensure Java 17 is installed
3. Run `MaternityApiApplication`
4. Open a browser and navigate to the required endpoint, for example:
    - `http://localhost:8080/api/patients/1/rooms`

## How to Run Tests
### Unit Tests
Run:
- `MaternityServiceTest`

### Integration Tests
Run:
- `MaternityControllerTest`

## Notes
- The external API currently contains `null` room numbers in `/RoomAllocations`
- The external API admission dates are historical, so some live endpoint calls return empty lists
- Business logic correctness is validated through unit tests using mocked data