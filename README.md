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

Run mvn clean package -DskipTests in the project root to create the jar file.  
Create application image after:  
docker build -t insurancequoteapp .

To run the service infrastructure, access src\main\docker folder and execute:  

docker-compose up

The application will run on 8080.  
The mongodb container will run on 27017.  
The localstack container will run on 4566.  
The wiremock container will run on 8088.

# Messaging Integration
### Inbound integration
- queue-insurance-policy-created (Receive an insurance policy created for an insurance quotation)
### Outbound integration
- queue-insurance-policy-received (Post an insurance quote created)

When submit a POST, a message with the last insurance quotation created will be sent to a third part application using AWS SQS queue.  
Also the API can receive a message with the insurance policy created for a update in database insurance quote entity.  

Example of publishing a message using localstack that will be received by the application:

```bash
aws --endpoint-url=http://localhost:4566 sqs send-message  --queue-url http://localhost:4566/000000000000/queue-insurance-policy-created  --profile localstack  --message-body '{
		"insurance_policy_id": "123a492c37e94a61726e8db8",
		"insurance_quote_id": "678a492c37e94a61726e8db8"
	}
}'
```

# Project Anatomy

Adapters (Ports):

In (REST)
- InsuranceQuoteController

In (Broker)
- InboundBroker

Out (Broker)
- OutboundBroker

Out (Persistence)
- InsuranceQuoteRepository

Application Inbound Port:
- InsuranceQuotationUseCase
- UpdateInsuranceQuotePolicy

Application Domain:
- InsuranceQuote
- InsurancePolicy

# Post payload example:
```bash
curl --location --request POST 'http://localhost:8080/insurance-quotes' \
--header 'Content-Type: application/json' \
--data-raw '{
    "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
    "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
    "category": "HOME",
    "total_monthly_premium_amount": 75.25,
    "total_coverage_amount": 825000.00,
    "coverages": {
        "Incêndio": 250000.00,
        "Desastres naturais": 500000.00,
        "Responsabiliadade civil": 75000.00
    },
    "assistances": [
        "Encanador",
        "Eletricista",
        "Chaveiro 24h"
    ],
    "customer": {
        "document_number": "36205578900",
        "name": "John Wick",
        "type": "NATURAL",
        "gender": "MALE",
        "date_of_birth": "1973-05-02",
        "email": "johnwick@gmail.com",
        "phone_number": 11950503030
    }
}'
```


