package com.remodelAi.stripe.service;

import com.remodelAi.stripe.exception.BadRequestException;
import com.remodelAi.stripe.exception.ResourceNotFoundException;
import com.remodelAi.stripe.models.StripeCustomer;
import com.remodelAi.stripe.repositories.StripeCustomerRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentMethodListParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StripeCustomerService {

    @Value("${stripe.secret.key}")
    private final String stripeSecretKey;

    private final StripeCustomerRepository stripeCustomerRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public StripeCustomer createCustomer(CustomerCreateParams customerCreateParams, String companyId) {
        try {
              Customer customer = Customer.create(customerCreateParams);
              return stripeCustomerRepository.save(StripeCustomer.builder().customer(customer).companyId(companyId).build());
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public StripeCustomer updateStripeCustomerInRemodelAi(String customerId, StripeCustomer stripeCustomer) {
            StripeCustomer foundCustomer = stripeCustomerRepository.getCustomerById(customerId);
            if(!ObjectUtils.isEmpty(foundCustomer)) {
               foundCustomer.setPaymentIntentList(stripeCustomer.getPaymentIntentList());
               return stripeCustomerRepository.save(foundCustomer);
            } else {
                throw new ResourceNotFoundException("resource not found with the given customer id" + customerId);
            }
    }



    public Customer retrieveCustomerFromStripe(String customerId) {
        try {
            return Customer.retrieve(customerId);
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public StripeCustomer retrieveCustomerFromRemodelAi(String customerId) {
          StripeCustomer stripeCustomer = stripeCustomerRepository.getCustomerById(customerId);
          if (stripeCustomer != null) {
              return stripeCustomer;
          } else {
              throw new ResourceNotFoundException("Customer with the given id : " + customerId);
          }
    }

    public List<PaymentMethod> getPaymentMethodsForCustomerFromStripe(String customerId) {
        PaymentMethodListParams params = PaymentMethodListParams.builder()
                .setCustomer(customerId)
                .build();
        try {
            return PaymentMethod.list(params).getData();
        } catch (StripeException e) {
           throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public Customer updateCustomer(String customerId, CustomerUpdateParams customerUpdateParams) {
        Customer customer = null;
        try {
            StripeCustomer existingCustomer = stripeCustomerRepository.getCustomerById(customerId);
            if (!ObjectUtils.isEmpty(existingCustomer)) {
                Customer updatedCustomer =  existingCustomer.getCustomer().update(customerUpdateParams);
                BeanUtils.copyProperties(updatedCustomer, existingCustomer.getCustomer());
                customer =  stripeCustomerRepository.updateCustomer(existingCustomer).getCustomer();
            }
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
        return customer;
    }

    public Customer deleteCustomer(String customerId) {
        Customer customer;
        try {
            customer = Customer.retrieve(customerId);
            Customer deletedCustomer =  customer.delete();
            if (deletedCustomer != null) {
                stripeCustomerRepository.deleteCustomer(customerId);
            }
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
        return customer;
    }

    public CustomerCollection retrieveListOfCustomers(Map<String, Object> params) {
        try {
          return Customer.list(params);
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }




}
