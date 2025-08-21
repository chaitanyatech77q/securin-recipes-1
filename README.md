# Securin Recipes Assessment – Java + Spring Boot Solution

This project implements the required **Recipe Data Collection and API Development**:

- Parse JSON, handle **NaN → NULL**
- Store in **PostgreSQL** (with `JSONB` for nutrients)
- Expose APIs:
  - `GET /api/recipes` (pagination, sorted by rating desc)
  - `GET /api/recipes/search` (title, cuisine, rating, total_time, calories operators)
- Ready schema, sample docker-compose, and examples.

> **Note**: During the official test window, follow the instruction that AI tools are blocked. Use these notes/code structure only before you start coding in your test environment.

---

## Quick start

### 1) Start PostgreSQL
```bash
docker compose up -d
```

This creates DB `securin` with schema from `schema.sql`.

### 2) Put the dataset
Place your `US_recipes.json` at:
```
src/main/resources/data/US_recipes.json
```

### 3) Run the app
```bash
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

On boot, it will ingest the JSON once.

### Render deployment (Blueprint)
This app supports Render deployment. Create a web service with:
- Build Command: `mvn clean package -DskipTests`
- Start Command: `java -jar target/recipes-0.0.1.jar`
The app binds to `PORT` automatically via `server.port=${PORT:8080}`.

---

## API

### 1) List (sorted by rating desc)
```
GET http://localhost:8080/api/recipes?page=1&limit=10
```

**Response**
```json
{
  "page": 1,
  "limit": 10,
  "total": 50,
  "data": [ { "...": "..." } ]
}
```

### 2) Search
```
GET http://localhost:8080/api/recipes/search?calories=<=400&title=pie&rating=>=4.5&total_time=<=120&page=1&limit=15
```

Supported numeric expressions:
- `>=x`, `<=x`, `>x`, `<x`, `=x`
- range: `100..400` (for calories/total_time)

Optional sorting:
```
&sort=rating&order=desc
```

---

## Submission checklist

- Source code (this repo)
- DB schema (`schema.sql`)
- API testing examples (above)
- **Folder name** for final submission: `StudentFullName_InstituteName_RecipesAssessment`
- Upload to Google Drive and share the consolidated link as instructed.
