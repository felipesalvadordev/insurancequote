package com.acme.insurancequote.service;

import com.acme.insurancequote.adapters.out.broker.OutboundBroker;
import com.acme.insurancequote.adapters.out.persistance.InsuranceQuoteRepository;
import com.acme.insurancequote.application.domain.dto.OfferDTO;
import com.acme.insurancequote.application.ports.inbound.InsuranceQuotationUseCase;
import com.acme.insurancequote.facade.CatalogFacade;
import com.acme.insurancequote.service.exceptions.LibBusinessException;
import com.acme.insurancequote.application.domain.InsuranceQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class InsuranceQuoteService implements InsuranceQuotationUseCase {

    private static final Logger log = LoggerFactory.getLogger(InsuranceQuoteService.class);

    private final CatalogFacade catalogFacade;
    private final InsuranceQuoteRepository insuranceQuoteRepository;
    private final OutboundBroker outboundBroker;

    public InsuranceQuoteService(CatalogFacade catalogFacade, InsuranceQuoteRepository insuranceQuoteRepository, OutboundBroker outboundBroker) {
        this.catalogFacade = catalogFacade;
        this.insuranceQuoteRepository = insuranceQuoteRepository;
        this.outboundBroker = outboundBroker;
    }

    @Override
    public InsuranceQuote postInsuranceQuotation(InsuranceQuote insuranceQuote) throws Exception {
        validate(insuranceQuote);
        var insuranceQuotation = insuranceQuoteRepository.save(insuranceQuote);

        try{
            outboundBroker.sendInsurancePolicyReceived(insuranceQuotation.toString());
        }
        catch (Exception ex){
            log.error("Erro ao publicar a mensagem {}. Erro de envio: {}. "+ insuranceQuotation.toString(), ex.getMessage());
        }

        return insuranceQuotation;
    }

    private void validate(InsuranceQuote insuranceQuote){

        OfferDTO catalogOfferDTO = catalogFacade.getOffering(insuranceQuote.getOfferId());

        for (Map.Entry<String, Long> coverageRequested : insuranceQuote.getCoverages().entrySet()) {
            if (catalogOfferDTO.getCoverages()!= null && !catalogOfferDTO.getCoverages().containsKey(coverageRequested.getKey()))
                throw new LibBusinessException("0001", "Coverage " + coverageRequested.getKey() + " not allowed for this offer.");

            var valueFromOfferCoverage = catalogOfferDTO.getCoverages().get(coverageRequested.getKey());

            if (coverageRequested.getValue() > valueFromOfferCoverage) {
                throw new LibBusinessException("0002", "Value from coverage " + coverageRequested.getKey() + " is greater than value allowed.");
            }
        }
    }
}
