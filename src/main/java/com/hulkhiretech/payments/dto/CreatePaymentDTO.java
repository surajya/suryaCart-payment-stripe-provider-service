package com.hulkhiretech.payments.dto;

import java.util.List;

import com.hulkhiretech.payments.pojo.LineItems;

import lombok.Data;

@Data
public class CreatePaymentDTO{
	private String successUrl;
	private String cancelUrl;
	
	private List<LineItemsDTO> lineItemDTO;
}
