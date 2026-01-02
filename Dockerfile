FROM maven:3.9.3-jdk-21 AS builder
WORKDIR /build
COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
