package com.patra.santu.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.patra.santu.service.model.Access;
import com.patra.santu.model.KeyValue;

@Repository
public class ApiDaoImpl implements ApiDao {

	@Autowired
	RestTemplate restTemplate;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseEntity<String> callApi(Access access) {
		String url = access.getUrl();

		List<String> uriVeriables = access.getPathVeriable();
		String uriVeriable1 = null;
		String uriVeriable2 = null;
		String uriVeriable3 = null;
		String uriVeriable4 = null;
		String uriVeriable5 = null;
		if (uriVeriables != null) {
			if (uriVeriables.size() >= 1) {
				uriVeriable1 = uriVeriables.get(0);
			}
			if (uriVeriables.size() >= 2) {
				uriVeriable2 = uriVeriables.get(1);
			}
			if (uriVeriables.size() >= 3) {
				uriVeriable3 = uriVeriables.get(2);
			}
			if (uriVeriables.size() >= 4) {
				uriVeriable4 = uriVeriables.get(3);
			}
			if (uriVeriables.size() >= 5) {
				uriVeriable5 = uriVeriables.get(4);
			}
		}

		List<KeyValue> params = access.getParams();
		if (params != null && !params.isEmpty()) {
			url = url + "?";
			for (KeyValue param : params) {
				url = url + param.getKey() + "=" + param.getValue() + "&";
			}
			url = (url == null) ? null : url.replaceAll(".$", "");
		}

		List<KeyValue> headers = access.getHeaders();
		HttpHeaders headerMap = new HttpHeaders();
		if (headers != null && !headers.isEmpty()) {
			headers.forEach(header -> {
				headerMap.add(header.getKey(), header.getValue());
			});
		}
		String body = access.getBody();
		HttpEntity request = new HttpEntity(body, headerMap);

		ResponseEntity<String> response = restTemplate.exchange(url, access.getMethod(), request, String.class,
				uriVeriable1, uriVeriable2, uriVeriable3, uriVeriable4, uriVeriable5);

		return response;
	}

}
