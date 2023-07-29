package com.remodelAi.stripe.service;

import com.remodelAi.stripe.exception.BadRequestException;
import com.remodelAi.stripe.exception.ResourceNotFoundException;
import com.remodelAi.stripe.models.StripeCustomer;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Address;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PaymentMethodUpdateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class StripePaymentmethodService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    private final StripeCustomerService stripeCustomerService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public PaymentMethod createPaymentMethod(String customerId, PaymentMethodCreateParams params) {
        if (ObjectUtils.isEmpty(params.getType()) && ObjectUtils.isEmpty(params.getCustomer())) {
            throw new BadRequestException("Type is a required field and it cannot be empty");
        }
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(params);
            if (!ObjectUtils.isEmpty(paymentMethod)) {
                StripeCustomer found = stripeCustomerService.retrieveCustomerFromRemodelAi(customerId);
            } else {
                throw new BadRequestException("Type is a required field and it cannot be empty");
            }
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public PaymentMethod getPaymentMethod(String paymentMethodId) {
        try {
            return PaymentMethod.retrieve(paymentMethodId);
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public PaymentMethod getCustomerPaymentMethod(String paymentMethodId) {
        try {
            return PaymentMethod.retrieve(paymentMethodId);
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public PaymentMethod updatePaymentMethod(String paymentMethodId, PaymentMethodUpdateParams params) {
        try {
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            if (ObjectUtils.isEmpty(paymentMethod)) {
                throw new ResourceNotFoundException("Resource not found exception");
            }
            return updatePaymentMethodData(paymentMethod, params);
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private PaymentMethod updatePaymentMethodData(PaymentMethod paymentMethod, PaymentMethodUpdateParams params) {
        PaymentMethod.BillingDetails billingDetails = new PaymentMethod.BillingDetails();
        PaymentMethod.Card card = new PaymentMethod.Card();
        PaymentMethod.Link link = new PaymentMethod.Link();
        PaymentMethod.UsBankAccount usBankAccount = new PaymentMethod.UsBankAccount();
        if (!ObjectUtils.isEmpty(params.getBillingDetails())) {
            billingDetails.setAddress((Address) params.getBillingDetails().getAddress());
            billingDetails.setEmail((String) params.getBillingDetails().getEmail());
            billingDetails.setName((String) params.getBillingDetails().getName());
            billingDetails.setPhone((String) params.getBillingDetails().getPhone());
            paymentMethod.setBillingDetails(billingDetails);
        }
        if (!ObjectUtils.isEmpty(params.getMetadata())) {
            paymentMethod.setMetadata((Map<String, String>) params.getMetadata());
        }
        if (!ObjectUtils.isEmpty(params.getCard())) {
            BeanUtils.copyProperties(params.getCard(), card);
            paymentMethod.setCard(card);
        }
        if (!ObjectUtils.isEmpty(params.getLink())) {
            BeanUtils.copyProperties(params.getLink(), link);
            paymentMethod.setLink(link);
        }
        if (!ObjectUtils.isEmpty(params.getUsBankAccount())) {
            BeanUtils.copyProperties(params.getUsBankAccount(), usBankAccount);
            paymentMethod.setUsBankAccount(usBankAccount);
        }
        try {
            return paymentMethod.update(params);
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }


}
