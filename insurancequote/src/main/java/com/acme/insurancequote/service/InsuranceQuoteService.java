package com.acme.insurancequote.service;

import com.acme.insurancequote.adapters.out.broker.OutboundBroker;
import com.acme.insurancequote.adapters.out.persistance.InsuranceQuoteRepository;
import com.acme.insurancequote.application.domain.InsurancePolicy;
import com.acme.insurancequote.application.domain.dto.OfferDTO;
import com.acme.insurancequote.application.domain.dto.ProductDTO;
import com.acme.insurancequote.application.ports.inbound.InsuranceQuotationUseCase;
import com.acme.insurancequote.application.ports.inbound.UpdateInsuranceQuotePolicy;
import com.acme.insurancequote.facade.CatalogFacade;
import com.acme.insurancequote.service.exceptions.LibBusinessException;
import com.acme.insurancequote.application.domain.InsuranceQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
@Transactional
public class InsuranceQuoteService implements InsuranceQuotationUseCase, UpdateInsuranceQuotePolicy {

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
        validateInsuranceQuoteOffer(insuranceQuote.getOfferId(), insuranceQuote.getCoverages());
        validateInsuranceQuoteProduct(insuranceQuote.getProductId());
    }

    private void validateInsuranceQuoteOffer(String offerID, Map<String, Long> insuranceQuoteCoverages){
        OfferDTO catalogOffer = catalogFacade.getOffering(offerID);

        if (!catalogOffer.getActive())
            throw new LibBusinessException("0001", "Offer is no longer active");

        for (Map.Entry<String, Long> coverageRequested : insuranceQuoteCoverages.entrySet()) {
            if (catalogOffer.getCoverages()!= null && !catalogOffer.getCoverages().containsKey(coverageRequested.getKey()))
                throw new LibBusinessException("0002", "Coverage " + coverageRequested.getKey() + " not allowed for this offer.");

            var valueFromOfferCoverage = catalogOffer.getCoverages().get(coverageRequested.getKey());

            if (coverageRequested.getValue() > valueFromOfferCoverage)
                throw new LibBusinessException("0003", "Value from coverage " + coverageRequested.getKey() + " is greater than value allowed.");
        }
    }

    private void validateInsuranceQuoteProduct(String productID){
        ProductDTO catalogProduct = catalogFacade.getProduct(productID);

        if (!catalogProduct.getActive())
            throw new LibBusinessException("0004", "Product is no longer active");
    }

    @Override
    public void updateInsuranceQuotePolicy(InsurancePolicy insurancePolicy) {
        var insuranceQuoteFromBase = insuranceQuoteRepository.findById(insurancePolicy.getInsuranceQuoteId());
        if (insuranceQuoteFromBase.isPresent()){
            insuranceQuoteFromBase.get().setInsurancePolicyId(insurancePolicy.getInsurancePolicyId());
            insuranceQuoteRepository.save(insuranceQuoteFromBase.get());
            log.info("Apólice do seguro atualizada no horário: {}", Instant.now());
        }
    }
}
