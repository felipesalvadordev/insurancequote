# Insurance Quotation Service
API REST for receiving and querying insurance quotes.  
There are two REST operations: 
- POST/insurance-quotes
- GET/insurance-quotes/{id}

The API also have inbound/outbount broker messages that will send and receive messages from queues.  

# About the project

The project uses the following technologies:  
- Spring Boot Java 17
- AWS SQS LocalStack 
- MongoDB
- JUnit & Mockito
- WireMock
- Docker

# Implementantion details
Created using the Hexagonal Architecture with REST inbound port, Broker inbound port and Persistance (Repository) outbound port.  
Use of commands (inbound port) to access the application rules.  
Use of DTOs for data transportation, validation and domain security. The domain entity is not exposed.  
Use of pattern FACADE for external integration. All the external config. is set only in the facade class.  
Use of a global exception handler to display exceptions more friendly to the user.  
Use of pattern BUILDER to create reusable objects for tests.  
Use the 12 FactorApp rule about config storage (https://12factor.net/pt_br/config). All configuration data are stored in yml files, properties and shell scripts. None in the base code.  

# Instructions to run
To execute the APIs integration mock, access mock directory using a terminal and execute:  

docker image build --tag catalog-mocks .  
docker container run -it --rm -p 8088:8080 --name catalog-mocks catalog-mocks

To run the service infrastructure, access src\main\docker folder using a terminal and execute:  

docker-compose up

# Messaging Integration
When submit a POST, a message will be sent to a third part application using AWS SQS queue.  
Also the API can receive a message with the insurance quote ID for a update in database.  

Example of publishing a message using localstack that will be received by the application:

aws --endpoint-url=http://localhost:4566 sqs send-message  --queue-url http://localhost:4566/000000000000/queue-insurance-policy-created  --profile localstack  --message-body '{
		"insurance_policy_id": "123a492c37e94a61726e8db8",
		"insurance_quote_id": "678a492c37e94a61726e8db8"
	}
}'

# Project Anatomy

Adapters (Ports):

In (REST)
- InsuranceQuoteController

In (Broker)
- InboundBroker

Out (Persistence)
- InsuranceQuoteRepository

Application Inbound Port:
- InsuranceQuotationUseCase
- UpdateInsuranceQuotePolicy

Application Domain:
- InsuranceQuote
- InsurancePolicy


