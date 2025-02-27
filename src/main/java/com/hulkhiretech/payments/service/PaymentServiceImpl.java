package com.hulkhiretech.payments.service;

import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.dto.CreatePaymentDTO;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	@Override
	public String createPayment(CreatePaymentDTO createPaymentDTO) {
		// TODO Auto-generated method stub
		log.info("Create payment dto : "+createPaymentDTO);
		return "return from PaymentService Implementation";
	}

}
