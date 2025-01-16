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
        validateInsuranceQuoteOffer(insuranceQuote);
        validateInsuranceQuoteProduct(insuranceQuote.getProductId());
    }

    private void validateInsuranceQuoteOffer(InsuranceQuote insuranceQuote){
        OfferDTO catalogOffer = catalogFacade.getOffering(insuranceQuote.getOfferId());

        if (catalogOffer == null)
            throw new LibBusinessException("0001", "Offer does not exist.");

        if (!catalogOffer.getActive())
            throw new LibBusinessException("0002", "Offer is no longer active.");

        for (Map.Entry<String, Long> coverageRequested : insuranceQuote.getCoverages().entrySet()) {
            if (catalogOffer.getCoverages()!= null && !catalogOffer.getCoverages().containsKey(coverageRequested.getKey()))
                throw new LibBusinessException("0003", "Coverage " + coverageRequested.getKey() + " not allowed for this offer.");

            var valueFromOfferCoverage = catalogOffer.getCoverages().get(coverageRequested.getKey());

            if (coverageRequested.getValue() > valueFromOfferCoverage)
                throw new LibBusinessException("0004", "Value from coverage " + coverageRequested.getKey() + " is greater than value allowed.");
        }

        if (!insuranceQuote.validateTotalCoverageAmount()){
            throw new LibBusinessException("0005", "Total coverage Amount out of value limit.");
        }

        for (String assistanceRequested : insuranceQuote.getAssistances()) {
            if (catalogOffer.getAssistances()!= null && !catalogOffer.getAssistances().contains(assistanceRequested))
                throw new LibBusinessException("0006", "Assistance " + assistanceRequested+ " not allowed for this offer.");
        }

        if (insuranceQuote.getTotalMonthlyPremiumAmount() > 0 && (
                insuranceQuote.getTotalMonthlyPremiumAmount() < catalogOffer.getMonthlyPremiumAmount().getMinAmount()||
                insuranceQuote.getTotalMonthlyPremiumAmount() > catalogOffer.getMonthlyPremiumAmount().getMaxAmount())){
            throw new LibBusinessException("0007", "Total monthly premium amount is not allowed for this offer.");
        }
    }

    private void validateInsuranceQuoteProduct(String productID){
        ProductDTO catalogProduct = catalogFacade.getProduct(productID);

        if (catalogProduct == null)
            throw new LibBusinessException("0008", "Product does not exist.");

        if (!catalogProduct.getActive())
            throw new LibBusinessException("0009", "Product is no longer active.");
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
