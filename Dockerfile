FROM maven:3.6.0-jdk-8-alpine as build

RUN mkdir -p /build
WORKDIR /build

COPY ./ ./

RUN mvn clean package -DskipTests=true

FROM openjdk:8

WORKDIR /build

COPY --from=build /build/target/fashion-be-0.0.1.jar ./fashion-be-0.0.1.jar

# This line should be commented when debuging
ENTRYPOINT ["java", "-jar", "/build/fashion-be-0.0.1.jar"]