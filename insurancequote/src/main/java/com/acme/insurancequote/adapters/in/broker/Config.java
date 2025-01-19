package com.acme.insurancequote.adapters.in.broker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class Config {
    @Value("${aws-sqs-localhost}")
    private String url;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {

        var credentials = EnvironmentVariableCredentialsProvider.create();

        return SqsAsyncClient.builder()
                .credentialsProvider(credentials)
                .endpointOverride(URI.create(url))
                .region(Region.US_EAST_1)
                .build();
    }
}

