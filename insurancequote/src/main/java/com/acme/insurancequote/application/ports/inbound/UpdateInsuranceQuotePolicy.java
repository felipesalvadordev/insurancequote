package com.acme.insurancequote.application.ports.inbound;

import com.acme.insurancequote.application.domain.InsurancePolicy;

public interface UpdateInsuranceQuotePolicy {
    void updateInsuranceQuotePolicy(InsurancePolicy insurancePolicy);
}
