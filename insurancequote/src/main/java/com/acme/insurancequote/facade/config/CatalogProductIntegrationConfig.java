package com.acme.insurancequote.facade.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CatalogProductIntegrationConfig {

    private final String protocol;
    private final String hostName;
    private final String port;
    private final String path;

    @Autowired
    public CatalogProductIntegrationConfig(@Value("${integration.catalog-product-service.protocol}") String protocol,
                                           @Value("${integration.catalog-product-service.hostname}") String hostname,
                                           @Value("${integration.catalog-product-service.port}") String port,
                                           @Value("${integration.catalog-product-service.path}") String path) {
        this.hostName = hostname;
        this.protocol = protocol;
        this.port = port;
        this.path = path;
    }

    public String buildURL(){
        return this.protocol + "://" + this.hostName + ":" + this.port + "/" + this.path;
    }
}
