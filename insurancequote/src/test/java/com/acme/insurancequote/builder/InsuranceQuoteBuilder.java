package com.acme.insurancequote.builder;

import com.acme.insurancequote.application.domain.InsuranceQuote;
import com.acme.insurancequote.application.domain.dto.InsuranceQuoteDTO;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class InsuranceQuoteBuilder {

    public InsuranceQuote buildInsuranceQuoteValid(){
        var insuranceQuote = new InsuranceQuote();
        insuranceQuote.setOfferId("123");
        insuranceQuote.setProductId("321");
        Map<String, Long> coverageInvalid = Map.ofEntries(entry("Garantia de Vida",500000L));
        insuranceQuote.setCoverages(coverageInvalid);
        insuranceQuote.setAssistances(List.of("Encanador"));
        insuranceQuote.setTotalCoverageAmount(500000L);
        insuranceQuote.setTotalMonthlyPremiumAmount(150L);
        return insuranceQuote;
    }

    public InsuranceQuoteDTO buildInsuranceQuoteDTOValid(){
        var insuranceQuoteDTO = new InsuranceQuoteDTO();
        insuranceQuoteDTO.setOfferId("123");
        insuranceQuoteDTO.setProductId("321");
        Map<String, Long> coverageInvalid = Map.ofEntries(entry("Garantia de Vida",500000L));
        insuranceQuoteDTO.setCoverages(coverageInvalid);
        insuranceQuoteDTO.setAssistances(List.of("Encanador"));
        insuranceQuoteDTO.setTotalCoverageAmount(500000L);
        insuranceQuoteDTO.setTotalMonthlyPremiumAmount(150L);
        return insuranceQuoteDTO;
    }
}
