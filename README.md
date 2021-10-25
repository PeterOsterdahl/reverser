# reverser
Wordsmith Inc Reverser
Taking over the world one reversal at a time

## The app
reverser is a simple BE app that exposes 2 endpoints for reversing and storing a sentence and retrieving the latest reversals performed.
The endpoints are reached at `/v1/reverse` with POST and GET respectively.

### Requirements:
- JDK 17
- Docker

### Building the JAR:
To build the JAR that will be run in a docker container you need to run the following command:

```
./gradlew build -x test
```
There are some integration tests that are dependant on a db-connection to be available thus we skip them when building


### Running the app:
To run the app we simply write:
```
docker-compose up
```
which will launch a spring-boot application together with a postgres db.
