# User Management App

Full-stack CRUD application for managing users.

- `backend/`: Spring Boot REST API with H2 database
- `frontend/`: React + Vite client

## Features

- Create a user
- View all users
- Get a user by ID
- Update user data
- Delete a user
- Manage users from a web UI

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- Lombok

### Frontend

- React
- Vite
- JavaScript
- Axios
- CSS

## Project Structure

```text
test_API/
├── backend/
└── frontend/
```

## Run Locally

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

The backend runs at `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend runs at `http://localhost:3000`.

## API Endpoints

### Get all users

- `GET /users`

### Get user by ID

- `GET /users/{id}`

### Create user

- `POST /users`

Example request body:

```json
{
  "name": "Peter",
  "email": "peter@gmail.com"
}
```

### Update user

- `PUT /users/{id}`

Example request body:

```json
{
  "name": "Peter",
  "email": "peter@gmail.com"
}
```

### Delete user

- `DELETE /users/{id}`

## Notes

- The backend uses an H2 in-memory database.
- Data may reset after application restart.
