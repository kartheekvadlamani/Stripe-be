package com.remodelAi.stripe.utils;

import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.util.ObjectUtils;

public class PaymentIntentValidation {

    public static void validatePaymentIntent(PaymentIntentCreateParams paymentIntentCreateParams) {
        if (ObjectUtils.isEmpty(paymentIntentCreateParams.getAmount())) {

        }
    }
}
