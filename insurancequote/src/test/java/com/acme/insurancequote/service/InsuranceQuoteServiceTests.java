package com.acme.insurancequote.service;

import com.acme.insurancequote.InsuranceQuoteApplication;
import com.acme.insurancequote.adapters.out.broker.OutboundBroker;
import com.acme.insurancequote.adapters.out.persistance.InsuranceQuoteRepository;
import com.acme.insurancequote.application.domain.InsurancePolicy;
import com.acme.insurancequote.application.domain.dto.OfferDTO;
import com.acme.insurancequote.application.domain.dto.ProductDTO;
import com.acme.insurancequote.builder.InsuranceQuoteBuilder;
import com.acme.insurancequote.facade.CatalogFacade;
import com.acme.insurancequote.service.exceptions.LibBusinessException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = InsuranceQuoteApplication.class)
@ActiveProfiles("test")
public class InsuranceQuoteServiceTests {

    @MockitoBean
    private CatalogFacade catalogFacade;

    @Autowired
    InsuranceQuoteService insuranceQuoteService;

    @Autowired
    private InsuranceQuoteRepository insuranceQuoteRepository;

    @MockitoBean
    OutboundBroker outboundBroker;

    void mockIntegrations(String offerID, String productID){
        var offerDTO = new OfferDTO();
        offerDTO.setActive(true);
        offerDTO.setAssistances(new ArrayList<>());
        offerDTO.getAssistances().add("Encanador");
        offerDTO.getAssistances().add("Chaveiro 24h");
        offerDTO.getAssistances().add("Assistência Funerária");
        offerDTO.setCoverages( Map.ofEntries(entry("Garantia de Vida",600000L)));

        OfferDTO.MonthlyPremiumAmount monthlyPremiumAmount = new OfferDTO.MonthlyPremiumAmount();
        monthlyPremiumAmount.setMinAmount(100L);
        monthlyPremiumAmount.setMaxAmount(1000L);
        offerDTO.setMonthlyPremiumAmount(monthlyPremiumAmount);

        Mockito.when(catalogFacade.getOffering(offerID)).thenReturn(offerDTO);

        var productDTO = new ProductDTO();
        productDTO.setActive(true);
        productDTO.setOffers(List.of("adc56d77-348c-4bf0-908f-22d402ee715c"));
        Mockito.when(catalogFacade.getProduct(productID)).thenReturn(productDTO);
    }

    @Test
    void testInsuranceQuotationWithValidDataShouldSaveInDatabase() throws Exception {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        mockIntegrations(insuranceQuote.getOfferId(), insuranceQuote.getProductId());

        //Act
        var insuranceQuotationCreated = insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        var insuranceQuotationFromBase = insuranceQuoteRepository.findById(insuranceQuotationCreated.getID());

        //Assert
        assertNotNull(insuranceQuotationFromBase);
        verify(outboundBroker, atLeast(1)).sendInsurancePolicyReceived(insuranceQuotationCreated.toString());
    }

    @Test
    void testInsuranceQuotationWithCoverageNotExistingInOfferCoverageShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        Map<String, Long> coverageNotContainedInOffer = Map.ofEntries(entry("Garantia de Vida",500000L));
        insuranceQuote.setCoverages(coverageNotContainedInOffer);

        var offerDTO = new OfferDTO();
        offerDTO.setActive(true);
        offerDTO.setCoverages( Map.ofEntries(entry("Incêndio",200000L)));
        Mockito.when(catalogFacade.getOffering(insuranceQuote.getOfferId())).thenReturn(offerDTO);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Coverage Garantia de Vida not allowed for this offer.", internalError.getErrorMessage());
    }

    @Test
    void testInsuranceQuotationWithCoverageWithInvalidValueShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        Map<String, Long> coverageNotContainedInOffer = Map.ofEntries(entry("Garantia de Vida",300000L));
        insuranceQuote.setCoverages(coverageNotContainedInOffer);

        var offerDTO = new OfferDTO();
        offerDTO.setActive(true);
        offerDTO.setCoverages( Map.ofEntries(entry("Garantia de Vida",200000L)));
        Mockito.when(catalogFacade.getOffering(insuranceQuote.getOfferId())).thenReturn(offerDTO);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Value from coverage Garantia de Vida is greater than value allowed.", internalError.getErrorMessage());
    }


    @Test
    void testInsuranceQuotationWithAssistanceNotExistingInOfferAssistanceShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        insuranceQuote.setAssistances(List.of("Encanador"));

        var offerDTO = new OfferDTO();
        offerDTO.setActive(true);
        offerDTO.setAssistances(new ArrayList<>());
        offerDTO.getAssistances().add("Chaveiro 24h");
        offerDTO.getAssistances().add("Assistência Funerária");
        offerDTO.setCoverages( Map.ofEntries(entry("Garantia de Vida",600000L)));
        Mockito.when(catalogFacade.getOffering(insuranceQuote.getOfferId())).thenReturn(offerDTO);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Assistance Encanador not allowed for this offer.", internalError.getErrorMessage());
    }

    @Test
    void testInsuranceQuotationWithOfferInactiveShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        var offerDTO = new OfferDTO();
        offerDTO.setActive(false);
        Mockito.when(catalogFacade.getOffering(insuranceQuote.getOfferId())).thenReturn(offerDTO);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Offer is no longer active.", internalError.getErrorMessage());
    }

    @Test
    void testInsuranceQuotationWithOfferNotFoundShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        Mockito.when(catalogFacade.getOffering("123")).thenReturn(null);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Offer does not exist.", internalError.getErrorMessage());
    }

    @Test
    void testInsuranceQuotationWithProductNotFoundShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        mockIntegrations(insuranceQuote.getOfferId(), insuranceQuote.getProductId());
        Mockito.when(catalogFacade.getProduct("321")).thenReturn(null);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Product does not exist.", internalError.getErrorMessage());
    }

    @Test
    void testInsuranceQuotationWithProductInactiveShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        var productDTO = new ProductDTO();
        productDTO.setActive(false);
        mockIntegrations(insuranceQuote.getOfferId(), insuranceQuote.getProductId());
        Mockito.when(catalogFacade.getProduct(insuranceQuote.getProductId())).thenReturn(productDTO);
        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Product is no longer active.", internalError.getErrorMessage());
    }

    @Test
    void testInsuranceQuotationWithInvalidTotalCoverageAmountShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        insuranceQuote.setTotalCoverageAmount(500001L);

        var offerDTO = new OfferDTO();
        offerDTO.setActive(true);
        offerDTO.setCoverages( Map.ofEntries(entry("Garantia de Vida",500000L)));
        Mockito.when(catalogFacade.getOffering(insuranceQuote.getOfferId())).thenReturn(offerDTO);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Total coverage Amount out of value limit.", internalError.getErrorMessage());
    }

    @Test
    void testInsuranceQuotationWithInvalidTotalPremiumAmountLowerThantMinAmountShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        insuranceQuote.setTotalMonthlyPremiumAmount(99L);

        var offerDTO = new OfferDTO();
        offerDTO.setActive(true);
        offerDTO.setCoverages( Map.ofEntries(entry("Garantia de Vida",500000L)));
        OfferDTO.MonthlyPremiumAmount monthlyPremiumAmount = new OfferDTO.MonthlyPremiumAmount();
        monthlyPremiumAmount.setMinAmount(100L);
        monthlyPremiumAmount.setMaxAmount(1000L);
        offerDTO.setMonthlyPremiumAmount(monthlyPremiumAmount);
        Mockito.when(catalogFacade.getOffering(insuranceQuote.getOfferId())).thenReturn(offerDTO);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Total monthly premium amount is not allowed for this offer.", internalError.getErrorMessage());
    }

    @Test
    void testInsuranceQuotationWithInvalidTotalPremiumAmountGreaterThantMaxAmountShouldThrownBusinessException() {
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        insuranceQuote.setTotalMonthlyPremiumAmount(1001L);

        var offerDTO = new OfferDTO();
        offerDTO.setActive(true);
        offerDTO.setCoverages( Map.ofEntries(entry("Garantia de Vida",500000L)));
        OfferDTO.MonthlyPremiumAmount monthlyPremiumAmount = new OfferDTO.MonthlyPremiumAmount();
        monthlyPremiumAmount.setMinAmount(100L);
        monthlyPremiumAmount.setMaxAmount(1000L);
        offerDTO.setMonthlyPremiumAmount(monthlyPremiumAmount);
        Mockito.when(catalogFacade.getOffering(insuranceQuote.getOfferId())).thenReturn(offerDTO);

        //Act
        LibBusinessException internalError = assertThrows(LibBusinessException.class, () -> {
            insuranceQuoteService.postInsuranceQuotation(insuranceQuote);
        });
        //Assert
        assertEquals("Total monthly premium amount is not allowed for this offer.", internalError.getErrorMessage());
    }

    @Test
    void testUpdateInsuranceQuotePolicyShouldUpdateDatabase(){
        //Arrange
        var insuranceQuote = new InsuranceQuoteBuilder().buildInsuranceQuoteValid();
        var insuranceQuoteCreated = insuranceQuoteRepository.save(insuranceQuote);
        var insurancePolicy = new InsurancePolicy();
        insurancePolicy.setInsuranceQuoteId(insuranceQuoteCreated.getID());
        insurancePolicy.setInsurancePolicyId("123");
        //Act
        insuranceQuoteService.updateInsuranceQuotePolicy(insurancePolicy);
        //Assert
        var insuranceQuoteUpdated = insuranceQuoteRepository.findById(insuranceQuoteCreated.getID());
        assertNotNull(insuranceQuoteUpdated.get().getInsurancePolicyId());
    }
}
