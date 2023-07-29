package com.remodelAi.stripe.service;

import com.remodelAi.stripe.exception.BadRequestException;
import com.remodelAi.stripe.models.StripeCustomer;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentUpdateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class StripePaymentService {

    private final StripeCustomerService stripeCustomerService;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public PaymentIntent createPaymentIntent(String customerId, PaymentIntentCreateParams paymentIntentCreateParams) {
        PaymentIntent paymentIntent;
        try {
            StripeCustomer stripeCustomer = stripeCustomerService.retrieveCustomerFromRemodelAi(customerId);
            if (!ObjectUtils.isEmpty(stripeCustomer) && !ObjectUtils.isEmpty(paymentIntentCreateParams.getCustomer())
            && !ObjectUtils.isEmpty(stripeCustomer.getCustomer())) {
                PaymentIntentCreateParams.builder().setCustomer(stripeCustomer.getCustomer().toString()).build();
            }
            paymentIntent = PaymentIntent.create(paymentIntentCreateParams);

            if (!ObjectUtils.isEmpty(stripeCustomer) && !ObjectUtils.isEmpty(paymentIntent)) {
                if (!ObjectUtils.isEmpty(stripeCustomer.getPaymentIntentList())) {
                    stripeCustomer.getPaymentIntentList().add(paymentIntent);
                } else {
                    stripeCustomer.setPaymentIntentList(Collections.singleton(paymentIntent));
                }
            }
            stripeCustomerService.updateStripeCustomerInRemodelAi(customerId, stripeCustomer);
        } catch (StripeException e) {
            throw new BadRequestException("payment was not successful");
        }
        return paymentIntent;
    }

    public PaymentIntent retrievePaymentIntentById(String paymentIntentId) {
        try {
            return PaymentIntent.retrieve(paymentIntentId);
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public PaymentIntent cancelPaymentIntent(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            return paymentIntent.cancel();
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }


    public PaymentIntentCollection retrieveListOfPaymentIntents(String paymentIntentId, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 3);
        try {
            return PaymentIntent.list(params);
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public PaymentIntent updatePaymentIntent(String paymentIntentId, PaymentIntentUpdateParams paymentIntentUpdateParams) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            return paymentIntent.update(paymentIntentUpdateParams);
        } catch (StripeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
