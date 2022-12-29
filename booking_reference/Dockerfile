FROM python:3.10-alpine

RUN pip install flask
WORKDIR /srv
COPY app.py /srv/
CMD flask run --port 8082 --host 0.0.0.0
