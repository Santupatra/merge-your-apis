package com.patra.santu.service;

import java.util.List;

import com.patra.santu.api.model.Request;
import com.patra.santu.api.model.Response;

//@Service
public interface ApiService {

	List<Response> getResponse(Request request);

}
