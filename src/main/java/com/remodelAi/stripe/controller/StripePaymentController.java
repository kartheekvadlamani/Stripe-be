package com.remodelAi.stripe.controller;


import com.remodelAi.stripe.service.StripePaymentService;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentUpdateParams;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.remodelAi.stripe.constants.ServiceConstants.STRIPE_PAYMENT;

@RestController
@RequestMapping(value = "/api/v1/paymentIntents")
@Slf4j
@RequiredArgsConstructor
@Tag(name = STRIPE_PAYMENT, description = "Company payment registration information")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StripePaymentController {

    private final StripePaymentService stripePaymentService;

    @PostMapping("/create/customer/{id}")
    public PaymentIntent createPaymentIntent(@PathParam("id") String customerId,
                    PaymentIntentCreateParams paymentIntentCreateParams) {
       return stripePaymentService.createPaymentIntent(customerId, paymentIntentCreateParams);
    }

    @GetMapping("/get/{id}")
    public PaymentIntent getPaymentIntent(@PathParam("id") String paymentIntentId) {
        return stripePaymentService.retrievePaymentIntentById(paymentIntentId);
    }

    @PostMapping("/{id}/confirm")
    public PaymentIntent updatePaymentIntent(@PathParam("id") String paymentIntentId, PaymentIntentUpdateParams paymentIntentUpdateParams) {
        return stripePaymentService.updatePaymentIntent(paymentIntentId, paymentIntentUpdateParams);
    }

    @GetMapping("/list")
    public PaymentIntentCollection getListOfPaymentIntents(@PathParam("id") String paymentIntentId, @RequestParam("limit") Integer limit) {
        return stripePaymentService.retrieveListOfPaymentIntents(paymentIntentId, limit);
    }

    @PostMapping("cancel/{id}")
    public PaymentIntent updatePaymentIntent(@PathParam("id") String paymentIntentId) {
        return stripePaymentService.cancelPaymentIntent(paymentIntentId);
    }


}
