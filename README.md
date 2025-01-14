# Insurance Quotation Service
API REST for receiving and querying insurance quotes

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


