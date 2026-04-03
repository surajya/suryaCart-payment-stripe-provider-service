package com.payments.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpRequest {
	private HttpMethod method;
	private String url;
	private HttpHeaders headers;
	private MultiValueMap<String,String> requestBody;
}
