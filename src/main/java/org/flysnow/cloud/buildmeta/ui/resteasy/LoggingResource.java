package org.flysnow.cloud.buildmeta.ui.resteasy;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.flysnow.cloud.buildmeta.ui.resteasy.form.SuccessResponse;


@Controller
@Path(LoggingResource.LOG_URL)
public class LoggingResource {

    public static final String LOG_URL = "/ws/logging";
    
    /**
     * curl -v -X GET -H "Content-Type: application/json" http://localhost:8080/ws/logging/org.flysnow/DEBUG
     * @param logger
     * @param log_level
     * @return json of SuccessResponse
     * Example:
     * {
     *     "code":0,
     *     "message":"logger org.flysnow is set to level DEBUG successfully!"
     * }
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{logger}/{log_level}")
    public Response setLogger(@PathParam("logger") String logger, 
    		@PathParam("log_level") String log_level) {
    	LogManager.getLogger(logger).setLevel(Level.toLevel(log_level));
    	String message = String.format("logger %s is set to level %s successfully!", logger, log_level);
    	SuccessResponse retResponse = new SuccessResponse(message);
        return Response.status(200).entity(retResponse.toJson()).build();
    }
    
}
