FROM node:22-alpine AS frontend-build
WORKDIR /ui
COPY sm-ui/package*.json ./
RUN npm ci
COPY sm-ui/ ./
RUN npm run build

FROM maven:3.9.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
COPY --from=frontend-build /ui/dist ./src/main/resources/static
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S studentapp && adduser -S studentapp -G studentapp
USER studentapp
WORKDIR /app
COPY --from=backend-build /app/target/student-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
