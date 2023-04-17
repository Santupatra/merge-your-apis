package com.patra.santu.dao;

import org.springframework.http.ResponseEntity;

import com.patra.santu.api.model.Access;

public interface ApiDao {

	ResponseEntity<String> callApi(Access access);

}
