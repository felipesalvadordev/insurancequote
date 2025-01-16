package com.acme.insurancequote.controller;

import com.acme.insurancequote.InsuranceQuoteApplication;
import com.acme.insurancequote.application.domain.InsuranceQuote;
import com.acme.insurancequote.application.domain.dto.InsuranceQuoteDTO;
import com.acme.insurancequote.application.domain.dto.OfferDTO;
import com.acme.insurancequote.application.domain.dto.ProductDTO;
import com.acme.insurancequote.builder.InsuranceQuoteBuilder;
import com.acme.insurancequote.facade.CatalogFacade;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = InsuranceQuoteApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InsuranceQuoteControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private CatalogFacade catalogFacade;

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
    public void testInsuranceQuotationWithValidDataShouldReturnCreated() {
        //Arrange
        var insuranceQuoteDTO = new InsuranceQuoteBuilder().buildInsuranceQuoteDTOValid();
        mockIntegrations(insuranceQuoteDTO.getOfferId(), insuranceQuoteDTO.getProductId());
        HttpEntity<InsuranceQuoteDTO> body = new HttpEntity<>(insuranceQuoteDTO, null);

        final ResponseEntity<InsuranceQuote> response =
                restTemplate.exchange("/insurance-quotes",
                        HttpMethod.POST,
                        body,
                        InsuranceQuote.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void testInsuranceQuotationWithoutMandatoryFieldShouldReturnBadRequest() {
        //Arrange
        var insuranceQuoteDTO = new InsuranceQuoteBuilder().buildInsuranceQuoteDTOValid();
        insuranceQuoteDTO.setOfferId("");
        HttpEntity<InsuranceQuoteDTO> body = new HttpEntity<>(insuranceQuoteDTO, null);

        //Act
        final ResponseEntity<InsuranceQuote> response =
                restTemplate.exchange("/insurance-quotes",
                        HttpMethod.POST,
                        body,
                        InsuranceQuote.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testFindInsuranceQuotationCreatedShouldReturnOK() {
        //Arrange
        var insuranceQuoteDTO = new InsuranceQuoteBuilder().buildInsuranceQuoteDTOValid();
        mockIntegrations(insuranceQuoteDTO.getOfferId(), insuranceQuoteDTO.getProductId());
        HttpEntity<InsuranceQuoteDTO> body = new HttpEntity<>(insuranceQuoteDTO, null);

        final ResponseEntity<InsuranceQuote> responsePOST = restTemplate.exchange("/insurance-quotes",
                HttpMethod.POST,
                body,
                InsuranceQuote.class);

        final ResponseEntity<InsuranceQuote> responseGET =
                restTemplate.exchange("/insurance-quotes/" + Objects.requireNonNull(responsePOST.getBody()).getID() ,
                        HttpMethod.GET,
                        null,
                        InsuranceQuote.class);

        assertThat(responseGET.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testFindInsuranceQuotationNotCreatedShouldReturnNotFound() {
        //Act
        final ResponseEntity<InsuranceQuote> responseGET =
                restTemplate.exchange("/insurance-quotes/123",
                        HttpMethod.GET,
                        null, InsuranceQuote.class);

        //Assert
        assertThat(responseGET.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
