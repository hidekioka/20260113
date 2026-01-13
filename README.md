# Code Assessment

### Developer configuration

* To build the project:
    * Run
        * <code>mvn clean install</code>
    * Or build using your IDE of preference (IntelliJ is recommended)

* To run the database (note that the directory path might be different depending on the OS):
    * For Linux (not tested):
        * <code>docker run --name code-assessment-10012026-postgres -e POSTGRES_PASSWORD=password -p 5432:5432 -v pgdata:/var/lib/postgresql/data -d postgres:17-alpine</code>
    * For Windows:
        * <code>docker run --name code-assessment-10012026-postgres -e POSTGRES_PASSWORD=password -p 5432:5432 -v C:\docker\pgdata:/var/lib/postgresql/data -d postgres:17-alpine</code>

* To run the application:
    * Main class: CodeAssessmentApplication.java
    * Env vars:
        * <code>SPRING_DATASOURCE_PASSWORD=password;SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/postgres;SPRING_DATASOURCE_USERNAME=postgres;SPRING_JPA_HIBERNATE_DDL_AUTO=update
          </code>

### Production options

* Run the docker-compose command, the db is and the app will be running after that.
    * <code>docker compose up -d</code>

* API documentation:
    * After the application is running, there 3 options to see the API doc:
        * http://localhost:8080/swagger-ui/index.html
        * http://localhost:8080/v3/api-docs
        * http://localhost:8080/v3/api-docs.yaml

* There is a Postman collection for testing a few scenarios in code-assessment/src/test/resources
    * 20260113.postman_collection.json
    * recommended to run in order