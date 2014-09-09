package org.flysnow.cloud.buildmeta.ui.resteasy;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.application.BuildService;
import org.flysnow.cloud.buildmeta.domain.model.Repository;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.BuildMetadataServiceException;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorCode;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorResponse;
import org.flysnow.cloud.buildmeta.ui.resteasy.form.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

@Controller
@Path(RepositoryResource.REPOSITORY_URL)
public class RepositoryResource {
	private Logger logger = Logger.getLogger(RepositoryResource.class);
	
    public static final String REPOSITORY_URL = "/ws/repository";
    
    @Autowired
    BuildService buildService;
    
    /**
     * curl http://localhost:8080/ws/repository
     * Sample Ouputs: 
     * {
     *   "repos":[
     *             {"id":1,"repoUrl":"git@git.flysnow.org:ci/helloworld.git"},
     *             {"id":2,"repoUrl":"git@git.flysnow.org:ci/releasemanager.git"}
     *           ]
     * }
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getRepos() {
    	List<Repository> repositories = buildService.getRepos();
    	HashMap<String, List<Repository>> map = new HashMap<String, List<Repository>>();
    	map.put("repositories", repositories);
    	String jsonRepos = new Gson().toJson(map);
    	return jsonRepos;
    }
    
    /**
     * curl -v -X DELETE -H "Content-Type: application/json" http://localhost:8080/ws/repository?repo=git@git.flysnow.org:ci/helloworld.git
     * @param repoUrl
     * @return
     */
    @DELETE
    public String deleteRepo(@QueryParam("repo_url") String repoUrl) {
    	if(repoUrl==null){
    		ErrorResponse errorResponse = new ErrorResponse();
    		errorResponse.setCode(ErrorCode.INVALID_PARAMETERS);
    		String message = "repo_url is required, ex: curl -v -X DELETE" +
    		                 "-H \"Content-Type: application/json\" " + 
    				         "http://{endpoint}/ws/repository?=git@git.flysnow.org:ci/helloworld.git";
    		errorResponse.setMessage(message);
    		
    		BuildMetadataServiceException exception = new BuildMetadataServiceException(errorResponse.toJson());
    		throw exception;
    	}
    	buildService.deleteRepo(repoUrl);
    	
    	String message = String.format("Repository %s is deleted successfully!", repoUrl);
    	SuccessResponse successResponse = new SuccessResponse(message);
    	return new Gson().toJson(successResponse);
    }
    
}
