package com.acme.insurancequote.application.domain.builder;

import com.acme.insurancequote.application.domain.dto.exception.ErrorDetailDTO;
import com.acme.insurancequote.application.domain.dto.exception.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;

public class ErrorResponseBuilder {

    public ExceptionDTO buildBadRequest(BindingResult bindingResult){
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setDetailDTOList(new ArrayList<>());

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
        return exceptionDTO;
    }
}
