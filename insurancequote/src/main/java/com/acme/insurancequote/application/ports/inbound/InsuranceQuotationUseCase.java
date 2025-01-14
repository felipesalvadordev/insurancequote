package com.acme.insurancequote.application.ports.inbound;

import com.acme.insurancequote.application.domain.InsuranceQuote;

public interface InsuranceQuotationUseCase {
    InsuranceQuote postInsuranceQuotation(InsuranceQuote insuranceQuote) throws Exception;
}
