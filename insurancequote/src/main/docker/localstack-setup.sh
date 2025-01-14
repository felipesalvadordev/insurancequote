#!/bin/sh
echo "Initializing localstack"

awslocal sqs create-queue --queue-name queue-insurance-policy-created