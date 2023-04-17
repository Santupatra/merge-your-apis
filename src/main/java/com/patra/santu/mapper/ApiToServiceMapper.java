package com.patra.santu.mapper;

import java.util.ArrayList;
import java.util.List;

import com.patra.santu.service.model.Access;

public class ApiToServiceMapper {

	public static Access convertAccess(com.patra.santu.api.model.Access access) {
		return new Access(access.getUrl(), access.getMethod(), access.getParams(), access.getHeaders(),
				access.getBody(), access.getPathVeriable(), access.getMapping(), null, null);
	}

	public static List<Access> convertAccessList(List<com.patra.santu.api.model.Access> accesses) {
		List<Access> serviceAccess = new ArrayList<>();
		for (com.patra.santu.api.model.Access access : accesses) {
			serviceAccess.add(convertAccess(access));
		}
		return serviceAccess;
	}
}
