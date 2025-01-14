package com.acme.insurancequote.application.domain;

import com.acme.insurancequote.application.domain.dto.OfferDTO;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Offer {

    private String id;
    private String productId;

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

    public OfferDTO.MonthlyPremiumAmount getMonthlyPremiumAmount() {
        return monthlyPremiumAmount;
    }

    public void setMonthlyPremiumAmount(OfferDTO.MonthlyPremiumAmount monthlyPremiumAmount) {
        this.monthlyPremiumAmount = monthlyPremiumAmount;
    }

    private Instant createdAt;
    private Map<String, Long> coverages;
    private List<String> assistances;
    private OfferDTO.MonthlyPremiumAmount monthlyPremiumAmount;

    public static class MonthlyPremiumAmount{
        private Long maxAmount;
        private Long minAmount;
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
