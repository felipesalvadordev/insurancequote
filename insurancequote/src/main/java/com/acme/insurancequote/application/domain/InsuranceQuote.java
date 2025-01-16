package com.acme.insurancequote.application.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InsuranceQuote {
    @Id
    private String ID;
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
    private Customer customer;

    public boolean validateTotalCoverageAmount(){
        long sumCoverages = 0L;

        for (var coverage : coverages.entrySet()){
            sumCoverages = sumCoverages + coverage.getValue();
        }

        return totalCoverageAmount <= sumCoverages;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static class Customer{

        private String name;
        private String type;
        private String gender;
        private Date dateOfBirth;
        private String email;
        private String documentNumber;

        public String getDocumentNumber() {
            return documentNumber;
        }

        public void setDocumentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(Date dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @Override
    public String toString() {
        return "InsuranceQuote{" +
                "ID='" + ID + '\'' +
                ", insurancePolicyId='" + insurancePolicyId + '\'' +
                ", productId='" + productId + '\'' +
                ", offerId='" + offerId + '\'' +
                ", category='" + category + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", totalMonthlyPremiumAmount=" + totalMonthlyPremiumAmount +
                ", totalCoverageAmount=" + totalCoverageAmount +
                ", coverages=" + coverages +
                ", assistances=" + assistances +
                ", customer=" + customer +
                '}';
    }
}
