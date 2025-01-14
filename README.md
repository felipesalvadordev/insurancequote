# Insurance Quotation Service
API REST for receiving and querying insurance quotes.  

# About the project

Project use some 12 FactorApp (https://12factor.net/pt_br/).  
All configuration data are stored in yml files, properties and shell scripts. None in the base code.  

The project uses the following technologies:  
- Spring Boot Java 17
- MongoDB
- AWS SQS LocalStack
- WireMock
- Docker

# Instructions to run
To execute the APIs integration mock, access mock directory using a terminal and execute:  

docker image build --tag catalog-mocks .  
docker container run -it --rm -p 8088:8080 --name catalog-mocks catalog-mocks

To run the service infrastructure, access src\main\docker folder using a terminal and execute:  

docker-compose up

# Project Anatomy

API Architecture Style: Hexagonal Architecture

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
- Offer


