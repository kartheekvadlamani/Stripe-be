package com.remodelAi.stripe.repositories;

import com.remodelAi.stripe.exception.ResourceNotFoundException;
import com.remodelAi.stripe.models.StripeCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StripeCustomerRepositoryImpl implements StripeCustomerRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public StripeCustomer save(StripeCustomer stripeCustomer) {
        return mongoTemplate.save(stripeCustomer);
    }

    @Override
    public StripeCustomer getCustomerById(String customerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(customerId));
        StripeCustomer stripeCustomer = mongoTemplate.findOne(query, StripeCustomer.class);
        if (stripeCustomer == null) {
            throw new ResourceNotFoundException("company with given Id is not found");
        }
        return stripeCustomer;
    }

    @Override
    public List<StripeCustomer> getListOfCustomers() {
        return mongoTemplate.findAll(StripeCustomer.class);
    }

    @Override
    public StripeCustomer updateCustomer(StripeCustomer stripeCustomer) {
        Query query= new Query();
        query.addCriteria(Criteria.where("id").is(stripeCustomer.getCustomer().getId()));
        StripeCustomer existingStripeCustomer = mongoTemplate.findOne(query, StripeCustomer.class);
        StripeCustomer updated = updateStripeCustomer(existingStripeCustomer);
        return mongoTemplate.save(updated);
    }

    private StripeCustomer updateStripeCustomer(StripeCustomer stripeCustomer) {
        StripeCustomer newCustomer = new StripeCustomer();
        BeanUtils.copyProperties(newCustomer, stripeCustomer);
        return newCustomer;
    }
    @Override
    public void deleteCustomer(String customerId) {

    }
}
