package org.flysnow.cloud.buildmeta.ui.resteasy;

import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.Configuration;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.application.BuildService;
import org.flysnow.cloud.buildmeta.application.CollectorService;
import org.flysnow.cloud.buildmeta.client.ESConnectionManager;
import org.flysnow.cloud.buildmeta.domain.model.Branch;
import org.flysnow.cloud.buildmeta.domain.model.Collector;
import org.flysnow.cloud.buildmeta.domain.model.CollectorResult;
import org.flysnow.cloud.buildmeta.domain.model.CollectorWithBLOBs;
import org.flysnow.cloud.buildmeta.publisher.ElasticSearchPublisher;
import org.flysnow.cloud.buildmeta.publisher.KafkaPublisher;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.BuildMetadataServiceException;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorCode;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorResponse;
import org.flysnow.cloud.buildmeta.ui.resteasy.form.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.sun.istack.NotNull;

@Controller
@Path(CollectorResource.URL)
public class CollectorResource {
	private Logger logger = Logger.getLogger(CollectorResource.class);

	public static final String URL = "/ws/collector";

	@Autowired
	CollectorService collectorService;

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
	@Path("/{scalr_url}/{env}/{role}/{c_type}")
	public String getCollectors(
			@PathParam("scalr_url") @NotNull String scalr_url,
			@PathParam("role") @NotNull String role,
			@PathParam("env") @NotNull String env,
			@PathParam("c_type") @NotNull String c_type) {
		if (role == null || env == null || c_type == null) {
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setCode(ErrorCode.INVALID_PARAMETERS);
			String message = "role,env and type is required, ex: curl http://{endpoint}/ws/{env}/{role}/{c_type}";
			errorResponse.setMessage(message);

			BuildMetadataServiceException exception = new BuildMetadataServiceException(
					errorResponse.toJson());
			throw exception;
		}
		logger.info("role=" + role);
		List<CollectorWithBLOBs> collectors = collectorService.getCollectors(
				scalr_url, role, env, c_type);

		HashMap<String, List<CollectorWithBLOBs>> map = new HashMap<String, List<CollectorWithBLOBs>>();
		map.put("collectors", collectors);
		String json = new Gson().toJson(map);
		System.out.println(json);
		return json;
	}

	@POST
	@Produces("application/json")
	@Path("/result")
	public Response result(@Context UriInfo uri, CollectorResult result) {
		logger.info("get result==" + result.getText());
		// publish to kafka
		ElasticSearchPublisher.getResource().send(result);
		return Response.status(200).entity(result.getText()).build();

	}
}
