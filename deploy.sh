#!/bin/bash
docker build -t documently-rest-service .
docker-compose -f docker-compose.yaml up -d