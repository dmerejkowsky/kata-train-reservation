# Readme for Java

* Make sure you have JDK 17 installed
* Make sure port 8081, 8082, and 8083 are free
* Open two consoles and run the two external web services with


```
cd booking_reference
./mvnw quarkus:build && java -jar target/quarkus-app/quarkus-run.jar
```

and

```
cd train_data
./mvnw quarkus:build && java -jar target/quarkus-app/quarkus-run.jar
```

To run ticket_office, you can use:

```
cd ticket_office
./mvnw compile exec:java -Dexec.mainClass=fr.arolla.trainreservation.Application
```

Open the project in `ticket_office` and run the tests.


