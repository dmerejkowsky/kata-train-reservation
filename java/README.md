# Readme for Java

## Running the three webservices:

* Make sure you have JDK 17 installed
* Make sure port 8081, 8082, and 8083 are free
* Open two consoles and run the two external web services with


```
cd booking_reference
./mvnw compile exec:java
```

and

```
cd train_data
./mvnw compile exec:java
```

To run the ticket_office service, you can use:

```
cd ticket_office
./mvnw compile exec:java
```

or use the appropriate "run configuration" from IntelliJ

## Running the tests

```
cd ticket_office
./mvnw compile exec:java
```

or use the appropriate "run configuration" from IntelliJ
