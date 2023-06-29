# Readme for Python

## Installing dependencies

Create a virtualenv, install the dependencies from `requirements.txt` in it, and
activate it.

## Running the three webservices:

* Make sure port 8081, 8082, and 8083 are free
* Open three consoles and run

```
$ cd train_data
$ python app.py
```

```
$ cd booking_reference
$ python app.py
```

```
$ cd ticket_office
$ python app.py
```

## Running the tests

Activate the virtualenv and run:

```
$ python -m pytest
```
