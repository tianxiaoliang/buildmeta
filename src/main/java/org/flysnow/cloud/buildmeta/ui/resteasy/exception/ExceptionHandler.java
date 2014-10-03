package org.flysnow.cloud.buildmeta.ui.resteasy.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

/**
 *
 */
@Provider
@Component
public class ExceptionHandler implements ExceptionMapper<BuildMetadataServiceException>{
	
	public Response toResponse(BuildMetadataServiceException ex) {
		return Response.serverError().entity(ex.getMessage()).build();
	}

}
