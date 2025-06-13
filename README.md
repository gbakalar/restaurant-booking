# Restaurant Booking API

This is a simple Java-based RESTful API for a restaurant booking system, built with:

- [MuServer](https://muserver.io/) – lightweight HTTP server
- Spring (Core, Context, JPA)
- H2 in-memory/file-based database
- Gson – for JSON parsing
- Maven – for build and dependency management

## 🚀 Features

- **POST /bookings** – Create a new booking
- **GET /bookings?date=YYYY-MM-DD** – Get all bookings for a specific date
- **PUT /bookings** – Update an existing booking
- **DELETE /bookings/{id}** – Delete a booking

Each booking contains:
- Customer name
- Table size
- Date (ISO format: `YYYY-MM-DD`)
- Time (`HH:mm`)

## 🧪 Testing with cURL

curl -X POST http://localhost:8080/bookings -H "Content-Type: application/json" -d '{"customerName":"Alice","tableSize":4,"date":"2025-06-15","time":"18:00"}'

curl -X PUT http://localhost:8080/bookings -H "Content-Type: application/json" -d '{"id": 1, "customerName":"Alice Updated","tableSize":5,"date":"2025-06-15","time":"20:00"}'

curl "http://localhost:8080/bookings?date=2025-06-15"

curl -X DELETE http://localhost:8080/bookings/1



