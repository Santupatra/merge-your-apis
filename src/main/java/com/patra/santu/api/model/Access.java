package com.patra.santu.api.model;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;

import lombok.Data;

@Data
public class Access {

	private String url;
	private HttpMethod method;
	private List<KeyValue> params;
	private List<KeyValue> headers;
	private String body;
	private List<String> pathVeriable;
	private Mapping mapping;
	private Map<?, ?> singleResponse;
	private List<Map<?, ?>> listResponse;

}
