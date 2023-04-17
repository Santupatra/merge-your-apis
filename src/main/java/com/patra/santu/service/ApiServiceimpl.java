package com.patra.santu.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patra.santu.api.model.Mapping;
import com.patra.santu.api.model.Request;
import com.patra.santu.api.model.Response;
import com.patra.santu.api.model.Type;
import com.patra.santu.dao.ApiDao;
import com.patra.santu.mapper.ApiToServiceMapper;
import com.patra.santu.model.KeyValue;
import com.patra.santu.service.model.Access;

@Service
public class ApiServiceimpl implements ApiService {

	@Autowired
	private ApiDao apiDao;

	@Override
	public List<Response> getResponse(Request request) {
		List<Response> responses = new ArrayList<>();
		Type type = request.getType();
		List<ResponseEntity<String>> callNormals = null;
		switch (type) {
		case NORMAL:
			List<Access> accessList = ApiToServiceMapper.convertAccessList(request.getAccess());
			
			callNormals = callNormal(accessList);
		default:

		}
		for (ResponseEntity<String> callNormal : callNormals) {
			Response response = new Response();
			HttpStatus statusCode = callNormal.getStatusCode();
			response.setStatusCode(statusCode);
			// HttpHeaders headers = callNormal.getHeaders();
			// response.setHeaders(headers);
			String body = callNormal.getBody();
			if (body != null) {
				List<Map<?, ?>> listObject = new ArrayList<>();
				try {
					if (body.startsWith("[")) {
						listObject = new ObjectMapper().readValue(body, new TypeReference<List<Map<?, ?>>>() {
						});
					} else {
						listObject.add(new ObjectMapper().readValue(body, Map.class));
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				response.setBody(listObject);
				responses.add(response);
			}

		}
		return responses;
	}

	private List<ResponseEntity<String>> callNormal(List<Access> accesses) {
		List<ResponseEntity<String>> responseList = new ArrayList<ResponseEntity<String>>();
		Access access1 = accesses.get(0);
		ResponseEntity<String> response1 = apiDao.callApi(access1);
		responseList.add(response1);
		Access access2 = null;
		if (accesses.size() > 1)
			access2 = accesses.get(1);
		if (access2 != null && response1 != null) {
			setResponseToNextAccess(access2, response1);
			List<Map<?, ?>> listResponse = access2.getListResponse();
			for (int n = 0; n < listResponse.size(); n++) {
				mappingKeyValues(access2, listResponse.get(n));
				ResponseEntity<String> response2 = apiDao.callApi(access2);
				responseList.add(response2);
			}
		}

		return responseList;
	}

	private void setResponseToNextAccess(Access access, ResponseEntity<String> response) {
		String body = response.getBody();
		if (body != null) {
			Map<?, ?> singleResponse = null;
			List<Map<?, ?>> listResponse = new ArrayList<>();
			try {
				if (body.startsWith("[")) {
					listResponse = new ObjectMapper().readValue(body, new TypeReference<List<Map<?, ?>>>() {
					});
				} else {
					singleResponse = new ObjectMapper().readValue(body, Map.class);
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			if (singleResponse != null) {
				access.setSingleResponse(singleResponse);
				listResponse.add(singleResponse);
			}
			if (listResponse != null) {
				access.setListResponse(listResponse);
			}
		}
	}

	private void mappingKeyValues(Access access, Map<?, ?> response) {
		Mapping mapping = access.getMapping();
		// path
		List<String> pathVeriable = mapping.getPathVeriable();
		if (pathVeriable != null) {
			List<String> existingPathVeriable = access.getPathVeriable();
			List<String> newPathVeriables = new ArrayList<String>();
			for (String key : pathVeriable) {
				String[] split = key.split("[.]");
				Map<?, ?> internal = getValueFromMap(response, split);
				String uriVeriable = internal.get(split[split.length - 1]).toString();
				newPathVeriables.add(uriVeriable);
			}
			if (existingPathVeriable != null) {
				existingPathVeriable.addAll(newPathVeriables);
				access.setPathVeriable(existingPathVeriable);
			} else {
				access.setPathVeriable(newPathVeriables);
			}

		}
//param
		List<KeyValue> params = mapping.getParams();
		if (params != null) {
			List<KeyValue> existingParams = access.getParams();
			List<KeyValue> newParams = new ArrayList<KeyValue>();
			for (KeyValue keyValue : params) {
				String[] split = keyValue.getValue().split("[.]");
				Map<?, ?> internal = getValueFromMap(response, split);
				String value = internal.get(split[split.length - 1]).toString();
				keyValue.setValue(value);
				newParams.add(keyValue);
			}
			if (existingParams != null) {
				existingParams.addAll(newParams);
				access.setParams(existingParams);
			} else {
				access.setParams(newParams);
			}
		}
//body
		List<String> bodyKeys = mapping.getBody();
		String body = access.getBody();
		if (bodyKeys != null && body != null) {
			List<String> bodyVeriables = new ArrayList<String>();
			for (String key : bodyKeys) {
				String[] split = key.split("[.]");
				Map<?, ?> internal = getValueFromMap(response, split);
				Object value = internal.get(split[split.length - 1]);
				bodyVeriables.add(value.toString());
			}
			MessageFormat.format(body, bodyVeriables);
		}
	}

	private Map<?, ?> getValueFromMap(Map<?, ?> singleResponse, String[] split) {
		// Map<?, ?> internal = singleResponse;
		for (int s = 0; s < split.length - 1; s++) {
			singleResponse = (Map<?, ?>) singleResponse.get(split[s]);
		}
		return singleResponse;
	}

}
