package com.acme.insurancequote.adapters.in.rest;

import com.acme.insurancequote.adapters.out.persistance.InsuranceQuoteRepository;
import com.acme.insurancequote.application.domain.InsuranceQuote;
import com.acme.insurancequote.application.domain.dto.ErrorDetailDTO;
import com.acme.insurancequote.application.domain.dto.ExceptionDTO;
import com.acme.insurancequote.application.domain.dto.InsuranceQuoteDTO;
import com.acme.insurancequote.application.ports.inbound.InsuranceQuotationUseCase;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/insurance-quotes")
public class InsuranceQuoteController {

    private static final Logger log = LoggerFactory.getLogger(InsuranceQuoteController.class);

    @Autowired
    private InsuranceQuotationUseCase insuranceQuotationUseCase;

    @Autowired
    private InsuranceQuoteRepository insuranceQuoteRepository;

    @PostMapping
    public ResponseEntity<?> postInsuranceQuotation(@Validated @Valid @RequestBody InsuranceQuoteDTO insuranceQuoteDTO, BindingResult bindingResult) throws Exception {

        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setDetailDTOList(new ArrayList<>());
        if (bindingResult.hasErrors()){
            for(ObjectError error : bindingResult.getAllErrors()){
                ErrorDetailDTO errorDetailDTO = new ErrorDetailDTO();
                if (error instanceof FieldError fieldError){
                    errorDetailDTO.setItem(fieldError.getField());
                }
                else{
                    errorDetailDTO.setItem(error.getCode());
                }
                errorDetailDTO.setDescription(error.getDefaultMessage());
                exceptionDTO.getDetailDTOList().add(errorDetailDTO);
            }

            exceptionDTO.setStatusCode(HttpStatus.BAD_REQUEST);
            exceptionDTO.setMessage("Foram encontradas validações");
            return ResponseEntity.badRequest().body(exceptionDTO);
        }

        log.info("Recebeu o payload: {}", insuranceQuoteDTO.toString());
        var insuranceQuote = convertDTOTOEntity(insuranceQuoteDTO, InsuranceQuote.class);
        return new ResponseEntity<InsuranceQuote>(insuranceQuotationUseCase.postInsuranceQuotation(insuranceQuote), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InsuranceQuote> get(@PathVariable("id") String id) {
        log.info("Requisitou o ID: {}", id);
        var insuranceQuotation = insuranceQuoteRepository.findById(id);
        return insuranceQuotation.map(insuranceQuote ->
                        new ResponseEntity<>(insuranceQuote, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public static <T> T convertDTOTOEntity(Object dto, Class<T> entityClass){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.convertValue(dto, entityClass);
    }
}
