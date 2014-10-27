package org.flysnow.cloud.buildmeta.ui.resteasy;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.application.BuildService;
import org.flysnow.cloud.buildmeta.application.CollectorService;
import org.flysnow.cloud.buildmeta.application.FarmService;
import org.flysnow.cloud.buildmeta.domain.model.Branch;
import org.flysnow.cloud.buildmeta.domain.model.Collector;
import org.flysnow.cloud.buildmeta.domain.model.CollectorResult;
import org.flysnow.cloud.buildmeta.domain.model.Farm;
import org.flysnow.cloud.buildmeta.publisher.KafkaPublisher;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.BuildMetadataServiceException;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorCode;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorResponse;
import org.flysnow.cloud.buildmeta.ui.resteasy.form.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

@Controller
@Path(ScalrResource.URL)
public class ScalrResource {
	private Logger logger = Logger.getLogger(ScalrResource.class);

	public static final String URL = "/ws/farm";

	@Autowired
	FarmService farmService;

	/**
	 * curl http://localhost:8080/ws/collector?role=someRole&env=1&c_type=text
	 * Sample Ouputs: { "collectors" : [
	 * {"id":16,"role":"","envid":"1","target":
	 * "/opt/some.conf","c_type":"text","interval"
	 * :"60","content":"cat /opt/some.conf"},
	 * {"id":16,"role":"","envid":"1","target"
	 * :"system env","c_type":"text","interval":"60","content":"env"},
	 * {"id":16,"role"
	 * :"","envid":"1","target":"sys.cpu","c_type":"metric","interval"
	 * :"60","content":"python script"} ] }
	 * 
	 * @param repoUrl
	 * @return
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getFarms(@QueryParam("scalr_endpoint") String scalr_endpoint) {
		if (scalr_endpoint == null) {
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setCode(ErrorCode.INVALID_PARAMETERS);
			String message = "scalr_end_point is required, ex: curl http://{endpoint}/ws/farm?scalr_endpoint=test.cloudfarms.net";
			errorResponse.setMessage(message);

			BuildMetadataServiceException exception = new BuildMetadataServiceException(
					errorResponse.toJson());
			throw exception;
		}
		logger.info("scalr_endpoint=" + scalr_endpoint);
		List<Farm> farms = farmService.getFarms(scalr_endpoint);

		HashMap<String, List<Farm>> map = new HashMap<String, List<Farm>>();
		map.put("farms", farms);
		String json = new Gson().toJson(map);
		System.out.println(json);
		return json;
	}

	@POST
	@Produces("application/json")
	@Path("/register")
	public Response registerFarm(@Context UriInfo uri, Farm farm) {
		logger.info("register farm=" + farm.getScalrFarmName());
		boolean done = farmService.save(farm);
		if (done)
			return Response.status(200).entity("registered").build();
		else
			return Response.status(200).entity("register failed").build();

	}
}
