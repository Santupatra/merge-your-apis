package com.patra.santu.api.model;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class Response {

	private HttpStatus statusCode;
	private HttpHeaders headers;
	private Object body;

}
