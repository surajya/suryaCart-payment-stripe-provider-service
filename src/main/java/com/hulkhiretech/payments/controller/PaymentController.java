package com.hulkhiretech.payments.controller;



import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.dto.CreatePaymentDTO;
import com.hulkhiretech.payments.pojo.CreatePaymentReq;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/v1/payments")
@Slf4j
public class PaymentController {
	
	
	private PaymentService paymentService;
	private ModelMapper modelMapper;
	
	
	PaymentController(PaymentService paymentService, ModelMapper modelMapper) {
		this.paymentService = paymentService;
		this.modelMapper = modelMapper;
	}
	

	@PostMapping
	public ResponseEntity<String> createPayment(@RequestBody CreatePaymentReq createPaymentRequest) {
		log.info("create payment Req: "+createPaymentRequest);
		log.info("Create Payment invoked");
		
		//List<LineItems> is mapped to List<LineItemsDTO> by custom mapper function
		modelMapper.typeMap(CreatePaymentReq.class, CreatePaymentDTO.class).addMappings(mapper -> 
	    mapper.map(src -> src.getLineItem(), CreatePaymentDTO::setLineItemDTO)
	);
		
		//convert createPaymentReq into createPaymentDTO.Class
		CreatePaymentDTO createPaymentDTO = modelMapper.map(createPaymentRequest,CreatePaymentDTO.class);
		
		log.info("Converted onto DTO CreatePayment dto: "+createPaymentDTO);
		String response = paymentService.createPayment(createPaymentDTO);
		
		
		
		return new ResponseEntity<String>("response of create payment: "+response, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public String getPayment(@PathVariable("id") String id) {
		log.info("Get Payment id: "+id);
		return "get payment successfully with id: "+id;
	}
	
	@GetMapping("/{id}/expire")
	public String expirePayment(@PathVariable("id") String id) {
		log.info("expire Payment id: "+id);
		return "expire payment successfully with id: "+id;
	}
}
