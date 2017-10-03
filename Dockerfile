FROM java:8

RUN mkdir /apphome
ADD ./target/ethtracker-1.0-SNAPSHOT.jar /apphome/
ADD ./twitter4j.properties /apphome/
WORKDIR /home/apphome
ENTRYPOINT ["java", "-Xmx200m", "-jar", "/apphome/ethtracker-1.0-SNAPSHOT.jar"]
