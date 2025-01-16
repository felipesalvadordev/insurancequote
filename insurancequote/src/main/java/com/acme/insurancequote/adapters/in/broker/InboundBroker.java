package com.acme.insurancequote.adapters.in.broker;

import com.acme.insurancequote.application.domain.InsurancePolicy;
import com.acme.insurancequote.application.ports.inbound.UpdateInsuranceQuotePolicy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InboundBroker {

    private final UpdateInsuranceQuotePolicy updateInsuranceQuotePolicy;
    private static final Logger log = LoggerFactory.getLogger(InboundBroker.class);

    public InboundBroker(UpdateInsuranceQuotePolicy updateInsuranceQuotePolicy) {
        this.updateInsuranceQuotePolicy = updateInsuranceQuotePolicy;
    }
    @SqsListener(value = "${inbound.insurance-policy-created}")
    public void listen(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var insurancePolicy = mapper.readValue(message, InsurancePolicy.class);
        updateInsuranceQuotePolicy.updateInsuranceQuotePolicy(insurancePolicy);
        log.info("Mensagem de política criada recebida. Payload: {}", message);
    }
}
