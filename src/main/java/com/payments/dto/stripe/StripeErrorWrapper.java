package com.payments.dto.stripe;

import lombok.Data;

@Data
public class StripeErrorWrapper {

	private StripeError error;

}
