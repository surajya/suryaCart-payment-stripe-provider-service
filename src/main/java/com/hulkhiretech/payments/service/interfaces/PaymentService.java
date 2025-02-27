package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.CreatePaymentDTO;

public interface PaymentService {
	public String createPayment(CreatePaymentDTO createPaymentDTO);
}
