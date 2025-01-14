#!/bin/sh
echo "Initializing localstack"

AWS_REGION=us-east-1

awslocal sqs create-queue --queue-name queue-insurance-policy-created  --region ${AWS_REGION}
awslocal sqs create-queue --queue-name queue-insurance-policy-received  --region ${AWS_REGION}