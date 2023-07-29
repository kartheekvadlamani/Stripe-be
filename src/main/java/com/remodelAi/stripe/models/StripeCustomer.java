package com.remodelAi.stripe.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document("StripeCustomer")
@Builder
public class StripeCustomer {

        @Id
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @JsonSerialize(using= ToStringSerializer.class)
        private ObjectId stripeCustomerId;

        private Customer customer;

        @Indexed(unique = true)
        private String companyId;

        private Set<PaymentIntent> paymentIntentList;

}
