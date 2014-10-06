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
import org.flysnow.cloud.buildmeta.domain.model.Branch;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.BuildMetadataServiceException;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorCode;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorResponse;
import org.flysnow.cloud.buildmeta.ui.resteasy.form.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

@Controller
@Path(BranchResource.BRANCH_URL)
public class BranchResource {
	private Logger logger = Logger.getLogger(BuildResource.class);
	
    public static final String BRANCH_URL = "/ws/branch";
    
    @Autowired
    BuildService buildService;
    
    /**
     * curl http://localhost:8080/ws/branch?repo_url=git@git.flysnow.org:ci/helloworld.git
     * Sample Ouputs:
     * {
     *   "repos" : [
			         {"id":16,"repoUrl":"git@git.flysnow.org:ci/helloworld.git","name":"master","created":1397981357323},
			         {"id":18,"repoUrl":"git@git.flysnow.org:ci/helloworld.git","name":"dev","created":1397981357324}
			       ]
     * }
     * 
     * @param repoUrl
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getBranches(@QueryParam("repo_url") String repoUrl) {
    	if(repoUrl==null){
    		ErrorResponse errorResponse = new ErrorResponse();
    		errorResponse.setCode(ErrorCode.INVALID_PARAMETERS);
    		String message = "repo_url is required, ex: curl http://{endpoint}/ws/branch?repo=git@git.flysnow.org:ci/helloworld.git";
    		errorResponse.setMessage(message);
    		
    		BuildMetadataServiceException exception = new BuildMetadataServiceException(errorResponse.toJson());
    		throw exception;
    	}
    	logger.info("repoUrl="+repoUrl);
    	List<Branch> branches = buildService.getBranches(repoUrl);
    	
    	HashMap<String, List<Branch>> map = new HashMap<String, List<Branch>>();
    	map.put("branches", branches);
    	String jsonBranches = new Gson().toJson(map);
    	System.out.println(jsonBranches);
    	return jsonBranches;
    }
    
    /**
     * curl -v -X DELETE -H "Content-Type: application/json" http://localhost:8080/ws/branch?repo_url=git@git.flysnow.org:ci/helloworld.git&branch=master
     * @param repoUrl, branch
     * @return
     */
    @DELETE
    public String deleteBranch(@QueryParam("repo_url") String repoUrl, @QueryParam("branch") String branch) {
    	//repoUrl and branch are required
    	if(repoUrl==null || repoUrl.equals("") || branch==null || branch.equals("")){
    		ErrorResponse errorResponse = new ErrorResponse();
    		errorResponse.setCode(ErrorCode.INVALID_PARAMETERS);
    		String message = "both repo_url and branch are required";
    		errorResponse.setMessage(message);
   
    		BuildMetadataServiceException exception = new BuildMetadataServiceException(errorResponse.toJson());
    		throw exception;
    	}

    	buildService.deleteBranch(repoUrl, branch);
    	
    	String message = String.format("Brach %s of repo %s is deleted successfully!", branch, repoUrl);
    	SuccessResponse successResponse = new SuccessResponse(message);
    	return new Gson().toJson(successResponse);
    }
    
}
