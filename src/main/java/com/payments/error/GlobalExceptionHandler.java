package com.payments.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.payments.constant.ErrorCodeEnum;
import com.payments.pojo.ErrorRes;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Handle StripeProviderException
    @ExceptionHandler(StripeProviderException.class)
    public ResponseEntity<ErrorRes> handleStripeProviderException(StripeProviderException ex) {
    	
    	log.info("stripe provider exception : "+ex);
    	
    	ErrorRes errorRes = new ErrorRes();
    	errorRes.setErrorCode(ex.getErrorCode());
    	errorRes.setErrorMessage(ex.getMessage());
    	
//        Map<String, String> errorResponse = new HashMap<>();
//        errorResponse.put("errorCode", ex.getErrorCode());
//        errorResponse.put("errorMessage", ex.getErrorMessage());
        log.info("errorResponse: " + errorRes);
        
        return new ResponseEntity<>(errorRes,ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleGenericException(Exception ex) {
    	
    	log.info("stripe provider exception : "+ex);
    	
    	ErrorRes errorRes = new ErrorRes();
    	errorRes.setErrorCode(ErrorCodeEnum.GENERIC_ERROR.getErrorCode());
    	errorRes.setErrorMessage(ErrorCodeEnum.GENERIC_ERROR.getErrorMessage());
    	
//        Map<String, String> errorResponse = new HashMap<>();
//        errorResponse.put("errorCode", ex.getErrorCode());
//        errorResponse.put("errorMessage", ex.getErrorMessage());
        log.info("errorResponse: " + errorRes);
        
        return new ResponseEntity<>(errorRes,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
