package com.remodelAi.stripe.controller;

import com.remodelAi.stripe.service.StripePaymentmethodService;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PaymentMethodUpdateParams;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.remodelAi.stripe.constants.ServiceConstants.STRIPE_PAYMENT;

@RestController
@RequestMapping(value = "/api/v1/paymentMethods")
@Slf4j
@RequiredArgsConstructor
@Tag(name = STRIPE_PAYMENT, description = "Customer payment method registration information")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StripePaymentMethodsController {

    private final StripePaymentmethodService stripePaymentmethodService;

    @PostMapping("/create/customer/{id}")
    public PaymentMethod createPaymentMethod(@PathParam("id") String customerId, PaymentMethodCreateParams params){
        return stripePaymentmethodService.createPaymentMethod(customerId, params);
    }

    @GetMapping("/{id}")
    public PaymentMethod getPaymentMethodData(@PathParam("id") String paymentMethodId) {
        return stripePaymentmethodService.getPaymentMethod(paymentMethodId);
    }

    @PostMapping("{id}/update")
    public PaymentMethod updatePaymentMethod(@PathParam("id") String paymentMethodId, PaymentMethodUpdateParams params){
        return stripePaymentmethodService.updatePaymentMethod(paymentMethodId, params);
    }

}
