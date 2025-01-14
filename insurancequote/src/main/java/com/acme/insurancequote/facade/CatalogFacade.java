package com.acme.insurancequote.facade;

import com.acme.insurancequote.application.domain.dto.OfferDTO;
import com.acme.insurancequote.application.domain.dto.ProductDTO;
import com.acme.insurancequote.facade.config.CatalogIntegrationConfig;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
public class CatalogFacade {

    private final CatalogIntegrationConfig urlConfig;
    private final RestTemplate restTemplate;

    public CatalogFacade(CatalogIntegrationConfig urlConfig, RestTemplate restTemplate) {
        this.urlConfig = urlConfig;
        this.restTemplate = restTemplate;
    }

    public OfferDTO getOffering(String offeringID) {
        try {

            var builder = UriComponentsBuilder.fromHttpUrl(this.urlConfig.buildURL())
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

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(this.urlConfig.buildURL())
                    .queryParam("id", productID);

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
