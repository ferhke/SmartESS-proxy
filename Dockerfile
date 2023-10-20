FROM amazoncorretto:11-alpine-jdk as MAVEN_BUILD

# Java arguments
RUN apk add --no-cache \
    git \
    maven \
    bash \
    curl wget

COPY pom.xml /build/
COPY src /build/

WORKDIR /build/
RUN mvn package -Dmaven.test.skip=true


FROM amazoncorretto:11

WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/smartess-proxy-0.0.1-SNAPSHOT-jar-with-dependencies.jar /app/
COPY ./bin/conf.ini /app

EXPOSE 502

CMD ["java","-jar" ,"/app/smartess-proxy-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
