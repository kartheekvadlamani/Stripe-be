package com.remodelAi.stripe.repositories;


import com.remodelAi.stripe.models.StripeCustomer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StripeCustomerRepository {

    StripeCustomer save(StripeCustomer stripeCustomer);

    StripeCustomer getCustomerById(String customerId);

    List<StripeCustomer> getListOfCustomers();

    StripeCustomer updateCustomer(StripeCustomer stripeCustomer);

    void deleteCustomer(String customerId);

}
