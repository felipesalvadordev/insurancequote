package com.acme.insurancequote.application.domain.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ExceptionDTO {
    private HttpStatus statusCode;
    private String message;
    private List<ErrorDetailDTO> detailDTOList;

    public ExceptionDTO(){

    }

    public ExceptionDTO(HttpStatus statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    public ExceptionDTO(HttpStatus statusCode, String message, List<ErrorDetailDTO> detailDTOList){
        this.statusCode = statusCode;
        this.message = message;
        this.detailDTOList = detailDTOList;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ErrorDetailDTO> getDetailDTOList() {
        return detailDTOList;
    }

    public void setDetailDTOList(List<ErrorDetailDTO> detailDTOList) {
        this.detailDTOList = detailDTOList;
    }
}
