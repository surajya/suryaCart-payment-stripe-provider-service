package com.payments.dto;

import java.util.List;

import com.payments.pojo.LineItems;

import lombok.Data;

@Data
public class CreatePaymentDTO{
	private String successUrl;
	private String cancelUrl;

	private List<LineItemsDTO> lineItemDTO;
}
