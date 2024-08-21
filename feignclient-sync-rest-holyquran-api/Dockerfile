FROM openjdk:17
RUN mkdir -p /usr/app/
ENV PROJECT_HOME /usr/app/
COPY target/feignclient-sync-rest-holyquran-api-1.0-SNAPSHOT.jar $PROJECT_HOME/feignclient-sync-rest-holyquran-api-1.0-SNAPSHOT.jar
WORKDIR $PROJECT_HOME
CMD ["java", "-jar", "./feignclient-sync-rest-holyquran-api:1.0-SNAPSHOT"]