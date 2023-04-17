package com.patra.santu.service.model;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;

import com.patra.santu.api.model.Mapping;
import com.patra.santu.model.KeyValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
