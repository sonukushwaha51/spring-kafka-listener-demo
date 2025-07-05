# spring-kafka-listener

Run the application locally:

# Without Docker Setup
`Add KAFKA_HOME in system variables and bin in path`

`Install Kafka 2.13.4`

`Open the directory in cmd`

`Update data.dir diectory in zookeper.properties in config folder`

`Update logs dir in server properties`

`Go to: cd ${kafka-directory}\kafka_2.13-3.9.1`

# Start Zookeeper and Kafka Server
`Start zookeper -> .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties`

`Start kafka server -> .\bin\windows\kafka-server-start.bat .\config\server.properties`

`Create kafka topic -> .\bin\windows\kafka-topics.bat --create --topic parking-inbound-sync --bootstrap-server localhost:9092`

`Start kafka producer -> .\bin\windows\kafka-console-producer.bat --topic parking-inbound-sync --bootstrap-server localhost:9092`

# With Docker Setup
!!Note:
**Zookeeper support has been removed from latest kafka-versions. You need to use version 7.2.1 or below for zookeeper and kafka for using zookeeper.**

`Create directory src/test/resources/docker`

`Create file docker-compose.yml in src/test/resources/docker`

# Run the below docker command to start the kafka and zookeeper containers:

Navigate to the directory where docker-compose.yml is located and run:

`docker-compose -f docker-compose.yml up -d`