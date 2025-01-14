# Insurance Quotation Service
API REST for receiving and querying insurance quotes

The project uses the following technologies:  
- Spring Boot Java 17
- MongoDB
- AWS SQS LocalStack
- WireMock
- Docker

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

Application Domain:
- InsuranceQuote
- Offer


