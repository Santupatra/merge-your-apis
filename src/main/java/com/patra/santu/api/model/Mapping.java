package com.patra.santu.api.model;

import java.util.List;

import com.patra.santu.model.KeyValue;

import lombok.Data;

@Data
public class Mapping {

	private List<String> pathVeriable;
	private List<KeyValue> params;
	private List<String> body;
}
