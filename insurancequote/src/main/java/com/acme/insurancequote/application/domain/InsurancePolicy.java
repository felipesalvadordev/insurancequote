package com.acme.insurancequote.application.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InsurancePolicy {
    private String insurancePolicyId;
    private String insuranceQuoteId;

    public String getInsuranceQuoteId() {
        return insuranceQuoteId;
    }

    public void setInsuranceQuoteId(String insuranceQuoteId) {
        this.insuranceQuoteId = insuranceQuoteId;
    }

    public String getInsurancePolicyId() {
        return insurancePolicyId;
    }

    public void setInsurancePolicyId(String insurancePolicyId) {
        this.insurancePolicyId = insurancePolicyId;
    }
}
