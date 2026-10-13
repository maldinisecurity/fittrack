# FitTrack Fitness Challenge Tracker

FitTrack is the commenced 5CS019 project (project idea 92). It records members and dated workouts, evaluates minutes and session goals, and provides a webpage dashboard connected to a local REST API. The original console demonstration is retained. The implementation uses Java 25, Maven, JUnit 5, Java HttpServer, Gson and MySQL JDBC.

## Build and run

In Eclipse: right-click the project → Maven → Update Project, then Run As → Maven build with goals `clean verify javadoc:javadoc`. Use the Java 25 JDK already configured for this project.

With JDK 25 and Maven on your terminal PATH:

```sh
mvn clean verify javadoc:javadoc
java -jar target/fittrack-0.0.1-SNAPSHOT.jar --memory
```

This workspace also includes `./scripts/mvn`, which locates Eclipse's JDK and uses Maven on PATH or the temporary Maven installation used during development. Install Maven normally for a permanent terminal setup. Development dependency downloads are isolated in `.build/m2` when using that temporary installation.

On this Mac, `./scripts/run --memory` locates Eclipse’s Java runtime automatically. `./scripts/run --demo` runs the console version.

Dashboard and API base URL: `http://127.0.0.1:8080`. Memory mode starts empty and loses records when stopped. Set `FITTRACK_PORT` to change the port. Stop with Ctrl+C. `--demo` runs the original counts, totals and longest-workout console demonstration instead of the server.

```sh
python3 scripts/api_demo.py
```

Run this against a running server to check real HTTP CRUD with reserved demo ID 900001. It first checks that the IDs are unused, then deletes its own records when finished. It works against memory mode or MySQL mode. It needs Python 3's standard library only.

## View and use the dashboard

Stop any older FitTrack instance with Ctrl+C, then run:

```sh
FITTRACK_PORT=8081 ./scripts/run --memory
```

Open **http://127.0.0.1:8081**. Start by adding a member, then record a workout and create a challenge. The four pages include forms, edit/delete controls, exercise totals and goal progress. JavaScript and CSS are included in the Java JAR; no separate frontend server or npm installation is needed.

The root `/` now serves the webpage. The previous JSON application information is at `/api`. Existing member, workout and challenge API addresses are unchanged.

Read the [beginner code guide](docs/beginner-code-guide.md) for the file labels, request flow and simple explanations.

## MySQL mode

No MySQL server was available during development. Follow [database setup](docs/mysql-setup.md), import [the schema and seed data](database/fittrack.sql), and configure the environment before starting `--mysql`. Passwords belong in the local environment and must not be added to the portfolio.

JDBC integration tests use H2 with MySQL compatibility mode. These tests validate SQL binding, CRUD, mapping and foreign-key behaviour; they do not establish that the supplied MySQL script has been executed successfully on MySQL.

## Portfolio materials

- [Marksheet checklist](docs/assessment-checklist.md): criterion weights, implementation evidence and remaining work.
- [Project report draft](docs/project-report.md): design, requirements, schema, validation and limitations.
- [Class diagram](docs/class-diagram.png) and [schema diagram](docs/schema-diagram.png): image-format diagrams; editable Mermaid sources are alongside them.
- [API contract and samples](docs/api.md).
- [Demonstration and viva guide](docs/viva-guide.md).
- Generated Javadoc: `target/reports/apidocs/index.html`.
- JUnit results: `target/surefire-reports/`.

The report needs your review, genuine MySQL screenshots, demonstration video and any references/disclosure required by your module before submission. The checklist records these outstanding items explicitly.

## Online hosting preparation

To access FitTrack from any computer, host the Java app and MySQL together. See the [online hosting guide](docs/online-hosting.md). A Dockerfile, portable schema and environment-variable example are included. Deployment is not complete: a hosting account/connection and live MySQL verification are still needed.

Cloud mode supports the provider's `PORT` variable and `FITTRACK_BIND_HOST`. Listening beyond the local computer requires `FITTRACK_ADMIN_USER` and `FITTRACK_ADMIN_PASSWORD` (at least 16 characters). Use the hosted app over HTTPS. Local runs without these variables continue to work as before.
