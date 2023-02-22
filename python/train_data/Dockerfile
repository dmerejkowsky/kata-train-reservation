FROM python:3.10-alpine

RUN pip install flask
COPY trains.json app.py /srv/
WORKDIR /srv
CMD flask run --port 8081 --host 0.0.0.0
