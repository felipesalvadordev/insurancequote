package com.acme.insurancequote.application.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class OfferDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("coverages")
    private Map<String, Long> coverages;
    @JsonProperty("assistances")
    private List<String> assistances;
    @JsonProperty("monthly_premium_amount")
    private MonthlyPremiumAmount monthlyPremiumAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {return active;}

    public void setActive(Boolean active) {this.active = active;}

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

    public MonthlyPremiumAmount getMonthlyPremiumAmount() {
        return monthlyPremiumAmount;
    }

    public void setMonthlyPremiumAmount(MonthlyPremiumAmount monthlyPremiumAmount) {
        this.monthlyPremiumAmount = monthlyPremiumAmount;
    }

    public static class MonthlyPremiumAmount{
        @JsonProperty("max_amount")
        private Long maxAmount;
        @JsonProperty("min_amount")
        private Long minAmount;
        @JsonProperty("suggested_amount")
        private Long suggestedAmount;

        public Long getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(Long minAmount) {
            this.minAmount = minAmount;
        }

        public Long getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(Long maxAmount) {
            this.maxAmount = maxAmount;
        }

        public Long getSuggestedAmount() {
            return suggestedAmount;
        }

        public void setSuggestedAmount(Long suggestedAmount) {
            this.suggestedAmount = suggestedAmount;
        }

    }
}
