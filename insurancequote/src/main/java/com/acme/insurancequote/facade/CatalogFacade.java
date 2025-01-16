package com.acme.insurancequote.facade;

import com.acme.insurancequote.application.domain.dto.OfferDTO;
import com.acme.insurancequote.application.domain.dto.ProductDTO;
import com.acme.insurancequote.facade.config.CatalogOfferingIntegrationConfig;
import com.acme.insurancequote.facade.config.CatalogProductIntegrationConfig;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
public class CatalogFacade {

    private final CatalogOfferingIntegrationConfig catalogOfferingIntegrationConfig;
    private final CatalogProductIntegrationConfig catalogProductIntegrationConfig;
    private final RestTemplate restTemplate;

    public CatalogFacade(CatalogOfferingIntegrationConfig catalogOfferingIntegrationConfig, CatalogProductIntegrationConfig catalogProductIntegrationConfig, RestTemplate restTemplate) {
        this.catalogOfferingIntegrationConfig = catalogOfferingIntegrationConfig;
        this.catalogProductIntegrationConfig = catalogProductIntegrationConfig;
        this.restTemplate = restTemplate;
    }

    public OfferDTO getOffering(String offeringID) {
        try {

            var builder = UriComponentsBuilder.fromHttpUrl(this.catalogOfferingIntegrationConfig.buildURL())
                        .path("/{id}")
                        .buildAndExpand(offeringID);

            ResponseEntity<OfferDTO> response = restTemplate
                    .exchange(builder.toUriString(),
                            HttpMethod.GET,
                    null, OfferDTO.class);

            return response.getBody();
        } catch (Exception e) {
            throw new InternalError("error.catalog.offering.integration");
        }
    }

    public ProductDTO getProduct(String productID) {
        try {

            var builder = UriComponentsBuilder.fromHttpUrl(this.catalogProductIntegrationConfig.buildURL())
                    .path("/{id}")
                    .buildAndExpand(productID);

            ResponseEntity<ProductDTO> response = restTemplate
                    .exchange(builder.toUriString(),
                            HttpMethod.GET,
                            null, ProductDTO.class);

            return Objects.requireNonNull(response.getBody());
        } catch (Exception e) {
            throw new InternalError("error.catalog.product.integration");
        }
    }
}
