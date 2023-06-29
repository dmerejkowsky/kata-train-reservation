# Readme for Python

## Installing dependencies

Create a virtualenv, install the dependencies from `requirements.txt` in it, and
activate it.

## Running the three webservices:

* Make sure port 8081, 8082, and 8083 are free
* Open three consoles and run

```
$ cd train_data
$ FLASK_DEBUG=true flask run --port 8081
```

```
$ cd booking_reference
$ FLASK_DEBUG=true flask run --port 8082
```

```
$ cd ticket_office
$ FLASK_DEBUG=true flask run --port 8083
```

## Running the tests

Activate the virtualenv and run:

```
$ pytest
```
