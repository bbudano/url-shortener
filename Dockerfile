FROM gradle:7.2.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
ARG CORS_ORIGIN
ENV CORS_ORIGIN=${CORS_ORIGIN}

RUN gradle build --no-daemon

FROM openjdk:17-oracle
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/url-shortener-*.jar /app/service.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/service.jar"]