FROM openjdk:11-jdk
VOLUME /tmp
ADD target/stackrs-service-0.0.1-SNAPSHOT.jar stackrs-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dserver.port=8080","-jar","/stackrs-service.jar"]
