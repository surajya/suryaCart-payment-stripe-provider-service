package com.payments.dto;

import lombok.Data;

@Data
public class LineItemsDTO {
	private String currency;
	private int quantity;
	private String productName;
	private int unitAmount;
}
