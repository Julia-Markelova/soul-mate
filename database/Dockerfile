FROM python:3.6-slim

RUN apt-get update -y && \
    apt-get install -y ncat postgresql-client

WORKDIR /opt/database
COPY requirements.txt .
RUN pip3 install --no-cache-dir -r requirements.txt
COPY . .

ENTRYPOINT ["alembic"]
