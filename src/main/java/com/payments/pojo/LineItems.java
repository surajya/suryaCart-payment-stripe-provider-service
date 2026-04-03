package com.payments.pojo;

import lombok.Data;

@Data
public class LineItems {
	private String currency;
	private int quantity;
	private String productName;
	private int unitAmount;
}
