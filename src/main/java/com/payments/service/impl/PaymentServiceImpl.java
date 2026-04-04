package com.payments.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;
import com.payments.constant.Constants;
import com.payments.constant.ErrorCodeEnum;
import com.payments.dto.CreatePaymentDTO;
import com.payments.dto.LineItemsDTO;
import com.payments.dto.PaymentDTO;
import com.payments.dto.stripe.StripeError;
import com.payments.dto.stripe.StripeErrorWrapper;
import com.payments.error.StripeProviderException;
import com.payments.http.HttpRequest;
import com.payments.http.HttpServiceEngine;
import com.payments.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	@Value("${stripe.create-session.url}")
	private String createSessionUrl;
	
	@Value("${stripe.get-session.url}")
	private String getSessionUrl;
	
	@Value("${stripe.expire-session.url}")
	private String expireSessionUrl;

	@Value("${stripe.password}")
	private String password;

	@Value("${stripe.apiKey}")
	private String apiKey;

	private HttpServiceEngine httpServiceEngine;

	private Gson gson;

	PaymentServiceImpl(HttpServiceEngine httpServiceEngine, Gson gson) {
		this.httpServiceEngine = httpServiceEngine;
		this.gson=gson;
	}

	
	@Override
	public PaymentDTO createPayment(CreatePaymentDTO createPaymentDTO) {
		log.info("Create payment dto : "+createPaymentDTO);
		
		if(createPaymentDTO.getSuccessUrl() == null) {
			log.info("error : success url is null");
			throw new StripeProviderException("3001","Success Url can not be NULL, pls provide success url",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		MultiValueMap<String,String> responseBody = new LinkedMultiValueMap<>();

		responseBody.add(Constants.MODE,Constants.PAYMENT);
		responseBody.add(Constants.SUCCESS_URL,"https://www.amazon.in/ref=nav_logo");
		responseBody.add(Constants.CANCEL_URL,"https://mercury-t2.phonepe.com/");

		//add responsebody from createPayemnt dto using forloop
		for(int i=0; i<createPaymentDTO.getLineItemDTO().size(); i++) {
			LineItemsDTO lineItems = createPaymentDTO.getLineItemDTO().get(i);
			responseBody.add(String.format(Constants.LINE_ITEMS_CURRENCY, i), lineItems.getCurrency());
			responseBody.add(String.format(Constants.LINE_ITEMS_PRODUCT_NAME, i), lineItems.getProductName());
			responseBody.add(String.format(Constants.LINE_ITEMS_UNIT_AMOUNT, i), String.valueOf(lineItems.getUnitAmount()));
			responseBody.add(String.format(Constants.LINE_ITEMS_QUANTITY, i), String.valueOf(lineItems.getQuantity()));
		}

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(apiKey,password);
		httpHeaders.add(httpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);



		HttpRequest httpRequest =HttpRequest.builder()
				.method(HttpMethod.POST)
				.url(createSessionUrl)
				.headers(httpHeaders)
				.requestBody(responseBody)
				.build();
		log.info("httpRequest : " + httpRequest);
		ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);
		
		//Handle the response
		PaymentDTO paymentDTO = processResponse(response);

		log.info("Returning form create payment method, paymentDTO : "+paymentDTO);

		return paymentDTO;
	}

	private PaymentDTO processResponse(ResponseEntity<String> response) {
		// handle for fail of pass
		
		log.info("processResponse : "+response);
		
		if(response.getStatusCode().is2xxSuccessful()) {
			PaymentDTO paymentDTO = gson.fromJson(response.getBody(), PaymentDTO.class);
			log.info("convert response object into PaymentDTO: "+paymentDTO);
			
			if(paymentDTO != null && paymentDTO.getUrl()!=null) {
				log.info("Got successful URL from Stripe paymentDTO: "+paymentDTO);
				return paymentDTO;
			}
			
			log.info("We Got 2xx status code, but not find URL");		}
		
		log.info("processResponse is failed, response is not valid");
		
		//Handle error response
		StripeErrorWrapper errorObj = gson.fromJson(response.getBody(), StripeErrorWrapper.class);
		log.info("Error response recieved, errorObj: " + errorObj);
		
		if(errorObj != null && errorObj.getError() != null) {
			log.info("Error response recieved, errorObj : " + errorObj);
			
			//For every error from stripe, we will return STRIPE_PSP_ERROR
			//
			
			throw new StripeProviderException(
					ErrorCodeEnum.STRIPE_PSP_ERROR.getErrorCode(),
					prepareErrorMessage(errorObj.getError()),
					HttpStatus.valueOf(response.getStatusCode().value())
					);
		}
		
		log.error("Generic Error, unable to get valid error object structure");		
		throw new StripeProviderException(
				ErrorCodeEnum.GENERIC_ERROR.getErrorCode(),
				ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
			
	}


	private String prepareErrorMessage(StripeError stripeError) {
		
		return stripeError.getType()
				+" : "+stripeError.getCode()
				+" : "+stripeError.getParam()
				+" : "+stripeError.getMessage();
	}


	@Override
	public PaymentDTO getPayment(String id) {
		log.info("getPaymentMethod invoked in Service class with id: " + id);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(apiKey,password);
		
		
		MultiValueMap<String,String> responseBody = new LinkedMultiValueMap<>();
		HttpRequest httpRequest =HttpRequest.builder()
				.method(HttpMethod.GET)
				.url(getSessionUrl.replace(Constants.SESSION_ID,id))
				.headers(httpHeaders)
				.requestBody(responseBody)
				.build();
		
		log.info("aAfter get request http request :"+httpRequest);
		ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);
		
		log.info("response entity:"+response);
		PaymentDTO paymentDTO = gson.fromJson(response.getBody(), PaymentDTO.class);
		return paymentDTO;
	}

	@Override
	public PaymentDTO expirePayment(String id) {
		// TODO Auto-generated method stub
		log.info("Expire PaymentMethod invoked in Service class with id: " + id);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(apiKey,password);
		
		
		MultiValueMap<String,String> responseBody = new LinkedMultiValueMap<>();
		HttpRequest httpRequest =HttpRequest.builder()
				.method(HttpMethod.POST)
				.url(expireSessionUrl.replace(Constants.SESSION_ID,id))
				.headers(httpHeaders)
				.requestBody(responseBody)
				.build();
		
		log.info("create http request for expire session:"+httpRequest);
		ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);
		
		log.info("expire session response:"+response);
		PaymentDTO paymentDTO = gson.fromJson(response.getBody(), PaymentDTO.class);
		
		return paymentDTO;
	}

}
