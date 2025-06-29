FROM openjdk:17-jdk-slim AS build
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw -DskipTests=true package

FROM openjdk:17-jdk-slim
WORKDIR trio-examples
COPY --from=build target/*.jar trio-examples-application.jar
ENTRYPOINT ["java", "-jar", "-DskipTests=true" , "trio-examples-application.jar"]
