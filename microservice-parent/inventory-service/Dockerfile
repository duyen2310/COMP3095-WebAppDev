#--------------
# Build Stage
#--------------
FROM gradle:8-jdk22-alpine AS builder

WORKDIR /home/gradle/src
COPY . .


RUN gradle build -x test


#--------------
#Package Stage
#--------------

FROM openjdk:22-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/inventory-service.jar

ENV POSTGRES_USER=admin \
    POSTGRES_PWD=password

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]

