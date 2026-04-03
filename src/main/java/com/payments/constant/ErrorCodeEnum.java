package com.payments.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
	
	GENERIC_ERROR("3000","Unable to process the Request, please try again later"),
	STRIPE_PSP_ERROR("3001","Stripe PSP Error Occurred"),
	UNABLE_TO_CONNECT_TO_STRIPE_PSP("3002","Unable to connect to stripe PSP");
	
	private String errorCode;
	private String errorMessage;
	
	private ErrorCodeEnum(String errorCode, String errorMessage) {
		this.errorCode =errorCode;
		this.errorMessage =errorMessage;
	}
}
