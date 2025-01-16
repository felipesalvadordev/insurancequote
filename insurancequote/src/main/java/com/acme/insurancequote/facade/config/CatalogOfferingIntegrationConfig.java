package com.acme.insurancequote.facade.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CatalogOfferingIntegrationConfig {

    private final String protocol;
    private final String hostName;
    private final String port;
    private final String path;

    @Autowired
    public CatalogOfferingIntegrationConfig(@Value("${integration.catalog-offering-service.protocol}") String protocol,
                                            @Value("${integration.catalog-offering-service.hostname}") String hostname,
                                            @Value("${integration.catalog-offering-service.port}") String port,
                                            @Value("${integration.catalog-offering-service.path}") String path) {
        this.hostName = hostname;
        this.protocol = protocol;
        this.port = port;
        this.path = path;
    }

    public String buildURL(){
        return this.protocol + "://" + this.hostName + ":" + this.port + "/" + this.path;
    }
}
