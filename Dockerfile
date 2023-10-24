FROM amazoncorretto:11

ARG tag=0.0.14-SNAPSHOT
ENV tag=${tag}
ENV CLASSPATH=/app/smartess-proxy.jar
WORKDIR /app
COPY ./target/smartess-proxy-${tag}-jar-with-dependencies.jar /app/smartess-proxy.jar
COPY ./bin/conf.ini /app

EXPOSE 502
EXPOSE 9909
ENTRYPOINT [ "bash", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -cp org.smartess.proxy.Engine -jar smartess-proxy.jar" ]