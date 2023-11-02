# SmartESS-proxy

**SmartESS-proxy** is a bridge between the VictorMax WiFi Plug Pro and MQTT, designed to facilitate data transmission to Home Assistant (HASS) or prometheus / grafana in addition to the native SmartESS cloud.

## Project Objective

The primary goal of this project is to enable the VictorMax WiFi Plug Pro to transmit data via MQTT to HASS. This is achieved by running the SmartESS-proxy and redirecting the DNS of `ess.eybond.com` to this proxy.
![smartess logo](https://github.com/stefb69/SmartESS-proxy/assets/4223252/f5516ec9-e075-464c-896d-6a6bfc7e1bad)

## Getting Started

### Prerequisites

- Java 11
- Maven

### Compilation

To compile the project, use the following Maven command:

```bash
mvn package -f "pom.xml"
```

This will generate a binary JAR file in target/smartess-proxy-0.0.14-SNAPSHOT-jar-with-dependencies.jar

#### Docker
Then you can create a container with `docker build . -t smartess-proxy:latest` or `docker-compose.build`


### Configuration

Before running the project, modify the parameters in the `conf.ini` file as per your requirements:

```ini
fakeClient=true
mqttServer=172.16.2.1
mqttPort=1883
enableMqttAuth=false
mqttUser=
mqttPass=
mqttTopic=victorMax/Inverter/
updateFrequency=10
```

### Execution

To run the project, use the following command:

```bash
java -jar smartess-proxy-0.0.14-SNAPSHOT-jar-with-dependencies.jar
```

#### Docker
run it with `docker run -p 502:502 smartess-proxy:latest` or `docker-conpose up` 


> **Note**: The `fakeClient` has also been adapted.

