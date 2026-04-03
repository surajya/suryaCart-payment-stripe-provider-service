package com.payments.pojo;

import lombok.Data;

@Data
public class PaymentReq {
	private String id;
	private String url;
	private String payment_status;
	private String status;
}
