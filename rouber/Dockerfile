FROM maven:3.8.6-openjdk-11 AS builder

# add pom.xml and source code
ADD pom.xml pom.xml
ADD src src/

# package jar
RUN mvn clean package -DskipTests

FROM openjdk:11.0.15

COPY --from=builder target/rouber-*.jar /app/app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/app.jar"]