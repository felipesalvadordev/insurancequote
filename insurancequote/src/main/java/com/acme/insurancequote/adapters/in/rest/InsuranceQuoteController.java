package com.acme.insurancequote.adapters.in.rest;

import com.acme.insurancequote.adapters.out.persistance.InsuranceQuoteRepository;
import com.acme.insurancequote.application.domain.InsuranceQuote;
import com.acme.insurancequote.application.domain.dto.InsuranceQuoteDTO;
import com.acme.insurancequote.application.ports.inbound.InsuranceQuotationUseCase;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insurance-quotes")
public class InsuranceQuoteController {

    private static final Logger log = LoggerFactory.getLogger(InsuranceQuoteController.class);

    @Autowired
    private InsuranceQuotationUseCase insuranceQuotationUseCase;

    @Autowired
    private InsuranceQuoteRepository insuranceQuoteRepository;

    @PostMapping
    public ResponseEntity<InsuranceQuote> postInsuranceQuotation(
            @RequestBody InsuranceQuoteDTO insuranceQuoteDTO, BindingResult bindingResult) throws Exception {
        log.info("Recebeu o payload: {}", insuranceQuoteDTO.toString());
        var insuranceQuote = convertDTOTOEntity(insuranceQuoteDTO, InsuranceQuote.class);
        return new ResponseEntity<InsuranceQuote>(insuranceQuotationUseCase.postInsuranceQuotation(insuranceQuote), HttpStatus.CREATED);
    }

    public static <T> T convertDTOTOEntity(Object dto, Class<T> entityClass){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.convertValue(dto, entityClass);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InsuranceQuote> get(@PathVariable("id") String id) {
        log.info("Requisitou o ID: {}", id);
        var insuranceQuotation = insuranceQuoteRepository.findById(id);
        return insuranceQuotation.map(insuranceQuote ->
                        new ResponseEntity<>(insuranceQuote, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
