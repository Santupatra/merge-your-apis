package com.patra.santu.api.model;

import java.util.List;

import lombok.Data;

@Data
public class Request {

	private Type type;
	private List<Access> access;
}
