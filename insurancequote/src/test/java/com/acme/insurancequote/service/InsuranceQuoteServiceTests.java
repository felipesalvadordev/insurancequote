package com.acme.insurancequote.service;

import com.acme.insurancequote.application.domain.InsuranceQuote;
import com.acme.insurancequote.application.domain.dto.OfferDTO;
import com.acme.insurancequote.facade.CatalogFacade;
import com.acme.insurancequote.service.exceptions.LibBusinessException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class InsuranceQuoteServiceTests {
    @MockitoBean
    private CatalogFacade catalogFacade;

    @Autowired
    InsuranceQuoteService insuranceQuoteService;

    @Test
    void testInsuranceQuotationWithCoverageNotExistingInOfferCoverageShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuote();
        insuranceQuote.setOfferId("123");
        Map<String, Long> coverageInvalid = Map.ofEntries(entry("Garantia de Vida",500000L));
        insuranceQuote.setCoverages(coverageInvalid);
        var offerDTO = new OfferDTO();
        offerDTO.setCoverages( Map.ofEntries(entry("Incêndio",200000L)));
        Mockito.when(catalogFacade.getOffering("123")).thenReturn(offerDTO);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Coverage Garantia de Vida not allowed for this offer.", internalError.getErrorMessage());
    }
}
