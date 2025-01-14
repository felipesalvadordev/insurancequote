package com.acme.insurancequote.adapters.out.broker;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.*;
import org.springframework.stereotype.Service;

@Service
public class OutboundBroker {

    private static final Logger log = LoggerFactory.getLogger(OutboundBroker.class);

    @Autowired
    private SqsTemplate sqsTemplate;

    @Value("${outbound.queue.url}")
    private String queueUrl;

    @Value("${outbound.queue.name}")
    private String outboundQueueName;

    public void sendInsurancePolicyReceived(String message) throws Exception {
        var SQS = queueUrl + "/" + outboundQueueName;
        log.info("Mensagem enviada para a fila de policies. Payload: {}", message);
        sqsTemplate.send(SQS, message);
    }
}
