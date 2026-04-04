package com.payments.service;

import com.payments.dto.CreatePaymentDTO;
import com.payments.dto.PaymentDTO;

public interface PaymentService {
	public PaymentDTO createPayment(CreatePaymentDTO createPaymentDTO);
	
	public PaymentDTO getPayment(String id);
	
	public PaymentDTO expirePayment(String idT);
}
