package com.acme.insurancequote.adapters.out.persistance;

import com.acme.insurancequote.application.domain.InsuranceQuote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceQuoteRepository extends MongoRepository<InsuranceQuote, String> {
}
