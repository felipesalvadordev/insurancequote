# Insurance Quotation Service
API REST for receiving and querying insurance quotes.  

# About the project

The project uses the following technologies:  
- Spring Boot Java 17
- AWS SQS LocalStack 
- MongoDB
- JUnit & Mockito
- WireMock
- Docker

# Instructions to run
To execute the APIs integration mock, access mock directory using a terminal and execute:  

docker image build --tag catalog-mocks .  
docker container run -it --rm -p 8088:8080 --name catalog-mocks catalog-mocks

To run the service infrastructure, access src\main\docker folder using a terminal and execute:  

docker-compose up

# Implementantion details
Created using the Hexagonal Architecture with REST inbound port, Broker inbound port and Persistance (Repository) outbound port.  
Use of DTOs for data transportation, validation and domain security. The domain entity is not exposed.  
Use of pattern FACADE for external integration. All the external config. is set only in the facade class.  
Use of a global exception handler to display exceptions more friendly to the user.  
Use the 12 FactorApp rule about config storage (https://12factor.net/pt_br/config). All configuration data are stored in yml files, properties and shell scripts. None in the base code.  

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
- Offer


