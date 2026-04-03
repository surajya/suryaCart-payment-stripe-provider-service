package com.payments.pojo;

import lombok.Data;

@Data
public class ErrorRes {
	private String errorCode;
	private String errorMessage;
}
