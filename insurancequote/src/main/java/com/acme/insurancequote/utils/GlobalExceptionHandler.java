package com.acme.insurancequote.utils;

import com.acme.insurancequote.application.domain.dto.exception.ExceptionDTO;
import com.acme.insurancequote.service.exceptions.LibBusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ExceptionDTO> NotFoundException(ChangeSetPersister.NotFoundException e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionDTO(HttpStatus.NOT_FOUND, e.getMessage())
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDTO> BadRequestException(BadRequestException e){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDTO(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(LibBusinessException.class)
    public ResponseEntity<ExceptionDTO> LibBusinessException(LibBusinessException e){

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ExceptionDTO(HttpStatus.UNPROCESSABLE_ENTITY, e.getErrorMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalError.class)
    public @ResponseBody ExceptionDTO InternalErrorException(HttpServletRequest res, Exception ex){
        return new ExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}