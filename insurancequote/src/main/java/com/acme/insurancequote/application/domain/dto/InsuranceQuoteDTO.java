package com.acme.insurancequote.application.domain.dto;

import com.acme.insurancequote.application.domain.InsuranceQuote;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InsuranceQuoteDTO  {
    private String insurancePolicyId;
    private String productId;
    private String offerId;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
    private Long totalMonthlyPremiumAmount;
    private Long totalCoverageAmount;
    private Map<String, Long> coverages;
    private List<String> assistances;
    private InsuranceQuote.Customer customer;

    public String getInsurancePolicyId() {
        return insurancePolicyId;
    }

    public void setInsurancePolicyId(String insurancePolicyId) {
        this.insurancePolicyId = insurancePolicyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getTotalMonthlyPremiumAmount() {
        return totalMonthlyPremiumAmount;
    }

    public void setTotalMonthlyPremiumAmount(Long totalMonthlyPremiumAmount) {
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
    }

    public Long getTotalCoverageAmount() {
        return totalCoverageAmount;
    }

    public void setTotalCoverageAmount(Long totalCoverageAmount) {
        this.totalCoverageAmount = totalCoverageAmount;
    }

    public Map<String, Long> getCoverages() {
        return coverages;
    }

    public void setCoverages(Map<String, Long> coverages) {
        this.coverages = coverages;
    }

    public List<String> getAssistances() {
        return assistances;
    }

    public void setAssistances(List<String> assistances) {
        this.assistances = assistances;
    }

    public InsuranceQuote.Customer getCustomer() {
        return customer;
    }

    public void setCustomer(InsuranceQuote.Customer customer) {
        this.customer = customer;
    }

}
