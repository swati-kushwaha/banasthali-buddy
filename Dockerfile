FROM eclipse-temurin:21-jdk AS builder
WORKDIR /build

# Install a compatible Maven in the builder image (some registries/mirrors
# don't expose the official `maven:*` variants). This downloads Apache Maven
# binary and symlinks it to `/usr/bin/mvn` so the existing build commands work.
RUN apt-get update && apt-get install -y curl tar ca-certificates \
	&& MAVEN_VERSION=3.9.4 \
	&& curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
		| tar -xz -C /opt \
	&& ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
	&& ln -s /opt/maven/bin/mvn /usr/bin/mvn \
	&& rm -rf /var/lib/apt/lists/*

COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
