package com.remodelAi.stripe.controller;

import com.remodelAi.stripe.models.StripeCustomer;
import com.remodelAi.stripe.service.StripeCustomerService;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.remodelAi.stripe.constants.ServiceConstants.STRIPE_PAYMENT;

@RestController
@RequestMapping(value = "/api/v1/customer")
@Slf4j
@RequiredArgsConstructor
@Tag(name = STRIPE_PAYMENT, description = "Company payment registration information")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StripeCustomerController {

    private final StripeCustomerService stripeCustomerService;

    @PostMapping("/create")
    public StripeCustomer createCustomer(CustomerCreateParams customerCreateParams, String companyId) {
        return stripeCustomerService.createCustomer(customerCreateParams, companyId);
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathParam("id") String customerId) {
        return stripeCustomerService.retrieveCustomerFromStripe(customerId);
    }

    @GetMapping("remodel/{id}")
    public StripeCustomer getCustomerFromRemodelAi(@PathParam("id") String customerId) {
        return stripeCustomerService.retrieveCustomerFromRemodelAi(customerId);
    }

    @PostMapping("/update/{id}")
    public Customer updateCustomer(@PathParam("id") String customerId, CustomerUpdateParams customerUpdateParams) {
        return stripeCustomerService.updateCustomer(customerId, customerUpdateParams);
    }

    @DeleteMapping("/delete/{id}")
    public Customer deleteCustomer(@PathParam("id") String customerId) {
        return stripeCustomerService.deleteCustomer(customerId);
    }

    @GetMapping("/list")
    public CustomerCollection getListOfCustomers(@RequestBody Map<String, Object> params) {
        return stripeCustomerService.retrieveListOfCustomers(params);
    }

    @GetMapping("paymentMethod/{id}")
    public List<PaymentMethod> getCustomerPaymentMethod(@PathParam("id") String customerId) {
        return stripeCustomerService.getPaymentMethodsForCustomerFromStripe(customerId);
    }
}
