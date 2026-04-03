package com.payments.controller;



import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payments.dto.CreatePaymentDTO;
import com.payments.dto.PaymentDTO;
import com.payments.pojo.CreatePaymentReq;
import com.payments.pojo.PaymentReq;
import com.payments.service.interfaces.PaymentService;

import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/v1/payments")
@Slf4j
public class PaymentController {
	
	//this is payments
	private PaymentService paymentService;
	private ModelMapper modelMapper;
	
	
	PaymentController(PaymentService paymentService, ModelMapper modelMapper) {
		this.paymentService = paymentService;
		this.modelMapper = modelMapper;
	}
	

	@PostMapping
	public ResponseEntity<PaymentReq> createPayment(@RequestBody CreatePaymentReq createPaymentRequest) {
		log.info("create payment Req: "+createPaymentRequest);
		log.info("Create Payment invoked");
		
		//List<LineItems> is mapped to List<LineItemsDTO> by custom mapper function
		modelMapper.typeMap(CreatePaymentReq.class, CreatePaymentDTO.class).addMappings(mapper -> 
	    mapper.map(src -> src.getLineItem(), CreatePaymentDTO::setLineItemDTO)
	);
		
		//convert createPaymentReq into createPaymentDTO.Class
		CreatePaymentDTO createPaymentDTO = modelMapper.map(createPaymentRequest,CreatePaymentDTO.class);
		
		log.info("Converted onto DTO CreatePayment dto: "+createPaymentDTO);
		PaymentDTO paymentDTO = paymentService.createPayment(createPaymentDTO);
		
		//change paymentdto into paymentReq;
		PaymentReq paymentReq = modelMapper.map(paymentDTO, PaymentReq.class);
		
		log.info("paymentReq: " + paymentReq);
		
		return new ResponseEntity<>(paymentReq, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PaymentReq> getPayment(@PathVariable("id") String id) {
		log.info("Get Payment method invoked with id: "+id);
		
		PaymentDTO paymentDTO = paymentService.getPayment(id);
		
		PaymentReq paymentReq = modelMapper.map(paymentDTO, PaymentReq.class);
		
		return new ResponseEntity<>(paymentReq, HttpStatus.OK);
	}
	
	@PostMapping("/{id}/expire")
	public ResponseEntity<PaymentReq> expirePayment(@PathVariable("id") String id) {
		log.info("expire Payment id: "+id);
		
		PaymentDTO paymentDTO = paymentService.expirePayment(id);
		
		PaymentReq paymentReq = modelMapper.map(paymentDTO, PaymentReq.class);
		
		return new ResponseEntity<>(paymentReq, HttpStatus.OK);
	}
}
