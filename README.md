#Insurance Quotation Service 

#Project Anatomy

API Architecture Style: Hexagonal Architecture

Adapters(Ports)
In (REST)
- InsuranceQuoteController

In (Broker)
- InboundBroker

Out (Persistence)
- InsuranceQuoteRepository

Application Inbound Port:
- InsuranceQuotationUseCase
