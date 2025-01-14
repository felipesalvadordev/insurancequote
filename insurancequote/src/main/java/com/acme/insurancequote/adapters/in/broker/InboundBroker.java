package com.acme.insurancequote.adapters.in.broker;

import com.acme.insurancequote.adapters.out.broker.OutboundBroker;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InboundBroker {

    private static final Logger log = LoggerFactory.getLogger(InboundBroker.class);

    @SqsListener("queue-insurance-policy-created")
    public void listen(String message) {
        log.info("Mensagem recebida da politicas criadas. Payload: {}", message);
    }
}
