package com.acme.insurancequote.application.domain.dto;

import com.acme.insurancequote.application.domain.InsuranceQuote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsuranceQuoteDTO  {
    private String insurancePolicyId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String productId;
    @NotNull
    @NotEmpty
    @NotBlank
    private String offerId;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
    @NotNull
    private Long totalMonthlyPremiumAmount;
    @NotNull
    private Long totalCoverageAmount;
    @NotEmpty
    @NotNull
    private Map<String, Long> coverages;
    @NotEmpty
    @NotNull
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
