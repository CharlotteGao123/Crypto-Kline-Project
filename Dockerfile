FROM openjdk:17-jdk-alpine
COPY target/CryptoKlineProject-1.0-SNAPSHOT.jar CryptoKlineProject-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/CryptoKlineProject-1.0-SNAPSHOT.jar"]