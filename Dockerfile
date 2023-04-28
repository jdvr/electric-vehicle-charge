FROM eclipse-temurin:11
RUN mkdir /opt/app
COPY build/libs/ecc-api-fat.jar /opt/app
CMD ["java", "-jar", "/opt/app/ecc-api-fat.jar.jar"]
