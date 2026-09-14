# Smart Student Management System

A full-stack recruiter demonstration for managing academic users, courses, class sections, enrollments, assessments, grades, and attendance through role-based dashboards.

The React application and Spring Boot REST API are packaged as one Docker service. A safe embedded H2 database is recreated and seeded when the application starts, allowing recruiters to explore the system without creating infrastructure or entering personal information.

## Live demo

[Open Live Demo](https://smart-student-demo.onrender.com) · [Portfolio](https://logicharbor.dev) · [View Source](https://github.com/AnushKumarP/student-management)

> Render's free service may need a short warm-up after a period of inactivity.

### Demo accounts

| Role | Email | Password |
| --- | --- | --- |
| Admin | `admin@demo.edu` | `Demo123!` |
| Teacher | `teacher@demo.edu` | `Demo123!` |
| Student | `student@demo.edu` | `Demo123!` |
| Parent | `parent@demo.edu` | `Demo123!` |

These credentials are intentionally public and work only with the disposable demonstration database. Do not reuse them for a production application.

## Features

- Admin, teacher, student, and parent dashboard experiences
- Demo login and signup endpoints
- Student, teacher, course, class, and enrollment management APIs
- Assessment, grade, and attendance workflows
- Responsive React and TypeScript interface
- Seeded academic data for immediate exploration
- Health monitoring with Spring Boot Actuator
- Multi-stage Docker production build
- Render Blueprint for automatic deployment

## Technology stack

| Layer | Technology |
| --- | --- |
| Frontend | React 19, TypeScript, Vite, Tailwind CSS, TanStack Query |
| Backend | Java 17, Spring Boot 3, Spring MVC, Spring Data JPA |
| Database | H2 in PostgreSQL compatibility mode |
| Testing | Spring Boot Test, frontend TypeScript build validation |
| Delivery | Maven, Docker, Render Blueprint |

## Application flow

| Stage | Responsibility |
| --- | --- |
| Browser | Renders the React single-page application |
| REST client | Sends same-origin requests under `/api` |
| Spring controllers | Validate requests and expose academic workflows |
| Service layer | Applies application rules and coordinates persistence |
| JPA repositories | Store and query seeded demonstration data |
| H2 database | Provides disposable in-memory persistence for the demo |

## Run locally

### Prerequisites

- Java 17+
- Node.js 20+
- Maven 3.9+ or the included Maven wrapper

Start the backend:

```bash
bash mvnw spring-boot:run
```

In a second terminal, start the frontend:

```bash
cd sm-ui
npm ci
npm run dev
```

Open `http://localhost:5173`. Vite proxies `/api` requests to Spring Boot on port `8080`.

## Build and run with Docker

```bash
docker build -t smart-student-demo .
docker run --rm -p 8080:8080 smart-student-demo
```

Open `http://localhost:8080`.

## API and health endpoints

- `POST /api/auth/login`
- `POST /api/auth/signup`
- `GET /api/students`
- `GET /api/teachers`
- `GET /api/courses`
- `GET /api/class-sections`
- `GET /api/enrollments`
- `GET /api/assessments`
- `GET /api/grades`
- `GET /api/attendance-sessions`
- `GET /api/attendance-records`
- `GET /actuator/health`

## Deploy on Render

1. Open the Render Dashboard and create a new Blueprint.
2. Connect this repository and select the `main` branch.
3. Use `render.yaml` as the Blueprint path.
4. Review the `smart-student-demo` free web service.
5. Deploy and wait for `/actuator/health` to report `UP`.

Render automatically rebuilds the application after new commits. Free services can sleep after inactivity, so the first visit can take approximately one minute.

## Demo limitations

- Data resets whenever the service restarts or sleeps.
- Authentication tokens are demonstration-only and are not validated on every API request.
- The application contains no real student or personal information.
- Production deployment would require PostgreSQL, server-side authorization, managed secrets, migrations, audit logging, and stronger automated test coverage.

## Author

**Anush Kumar** — Software Engineer specializing in Java, Spring Boot, REST APIs, AWS, Kafka, PostgreSQL, and React.

Portfolio: [LogicHarbor.dev](https://logicharbor.dev)
