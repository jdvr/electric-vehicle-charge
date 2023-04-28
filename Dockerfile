FROM eclipse-temurin:11 as builder

WORKDIR /code
COPY . /code
RUN ./gradlew build

FROM eclipse-temurin:11

RUN mkdir /opt/app
COPY --from=builder /code/build/libs/ecc-api-fat.jar /opt/app
EXPOSE 8080
CMD ["java", "-jar", "/opt/app/ecc-api-fat.jar"]
