package com.patra.santu.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patra.santu.api.model.Request;
import com.patra.santu.api.model.Response;
import com.patra.santu.service.ApiService;

@CrossOrigin
@RestController
@RequestMapping(value = "")
public class ApiController {

	@Autowired
	private ApiService apiService;

	@PostMapping(value = "/hit")
	public ResponseEntity<Object> getResponse(@RequestBody Request request) {

		try {
			List<Response> response = apiService.getResponse(request);
			return ResponseHandler.generateResponse("Successfully execute both of your Apis!", HttpStatus.OK, response);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
		}

	}

}
