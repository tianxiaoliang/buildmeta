package org.flysnow.cloud.buildmeta.ui.resteasy;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.application.BuildService;
import org.flysnow.cloud.buildmeta.domain.model.Branch;
import org.flysnow.cloud.buildmeta.domain.model.Build;
import org.flysnow.cloud.buildmeta.domain.model.Repository;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.BuildMetadataServiceException;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorCode;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorResponse;
import org.flysnow.cloud.buildmeta.ui.resteasy.form.BuildForm;
import org.flysnow.cloud.buildmeta.ui.resteasy.form.SuccessResponse;
import org.jboss.resteasy.annotations.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
@Path(BuildResource.BUILDS_URL)
public class BuildResource {
	private Logger logger = Logger.getLogger(BuildResource.class);
	
    public static final String BUILDS_URL = "/ws/builds";
    
    @Autowired
    BuildService buildService;
    
    /**curl -v -X POST -d"@build.txt" -H "Content-Type: application/json" http://localhost:8080/ws/builds/data
	 * build.txt
	 * class CreateBuildInfoRequest {
	 *      private Build build;

			public Build getBuild() {
				return build;
			}
		
			public void setBuild(Build build) {
				this.build = build;
			}
			
			public toJson(){
			    GsonBuilder gb = new GsonBuilder().setPrettyPrinting();
		        Gson gson = gb.create()
		        return gson.toString()
			}
	 * }
	 * {
          "build": {
                  "repoUrl": "git@git.flysnow.org:ci/helloworld.git",
                  "branch": "master",
                  "groupId": "org.flysnow.cloud",
                  "artifactId": "helloworld",
                  "packaging": "zip",
                  "type": "continuous",
                  "version": "0.1-20140122.065951-13",
                  "md5": "a935373d6cd982b090a0de5141259743",
                  "downloadUrl": "https://www.flysnow.org/nexus/content/repositories/snapshots/com/flysnow/cloud/helloworld/0.1-SNAPSHOT/helloworld-0.1-20140922.065951-1.zip",
                  "classifier": null
          }
       }
       Use Case: Register ComponentBuild Info, Register Repo Branch Info
    **/
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("data")
    public String saveBuild(@Context UriInfo uri, BuildForm buildForm) {
    	String jsonBuildForm = buildForm.toJSON();
    	
    	String repoUrl = buildForm.getRepoUrl();
    	String branchPara = buildForm.getBranch();
    	String version = buildForm.getVersion();
    	String md5 = buildForm.getMd5();
    	String downloadUrl = buildForm.getDownloadUrl();
    	
    	if(repoUrl==null || branchPara == null || version==null || md5==null || downloadUrl==null){
    		ErrorResponse errorResponse = new ErrorResponse();
    		errorResponse.setCode(ErrorCode.INVALID_PARAMETERS);
    		String message = "repoUrl, branch, version, md5, downloadUrl are required!";
    		errorResponse.setMessage(message);
    		
    		BuildMetadataServiceException exception = new BuildMetadataServiceException(errorResponse.toJson());
    		throw exception;
    	}
    	
    	logger.info(jsonBuildForm);
    	Build build = buildForm.toBuild();
    	buildService.save(build);
    	
    	Branch branch = new Branch();
    	branch.setName(build.getBranch());
    	branch.setRepoUrl(build.getRepoUrl());
    	branch.setCreated(System.currentTimeMillis());
    	buildService.saveBranch(branch);
    	
    	Repository repository = new Repository();
    	repository.setRepoUrl(build.getRepoUrl());
    	buildService.saveRepo(repository);
    	
    	List<Build> builds = buildService.getBuildByVersion(build.getVersion());
    	HashMap<String, List<Build>> map = new HashMap<String, List<Build>>();
    	map.put("builds", builds);
    	String jsonBuilds = new Gson().toJson(map);
    	return jsonBuilds;
    }
    
    /**
     * curl http://localhost:8080/ws/builds/data?ids=1 | python -mjson.tool
     * curl http://localhost:8080/ws/builds/data?version=0.1-20140722.065951-16 | python -mjson.tool
     * curl -v -X GET -H "Content-Type: application/json" http://localhost:8080/ws/builds/data?repo=git@git.flysnow.org:ci/helloworld.git&branch=master
     * 
     *           ] 
     * @param ids
     * @param version
     * @param repo
     * @param branch
     * @return jsonListBuilds
     *         {"builds":[
     *             {
     *               "id":3,
     *               "repoUrl":"git@git.flysnow.org:ci/helloworld.git",
     *               "branch":"master",
     *               "groupId":"org.flysnow.cloud",
     *               "artifactId":"helloworld-webconsole",
     *               "packaging":"zip",
     *               "type":"continuous",
     *               "version":"0.1-20140122.065951-14",
     *               "md5":"a935373d3",
     *               "downloadUrl":"https://nexus.flysnow.org/nexus/content/repositories",
     *               "classifier":"",
     *               "created":1405857094689
     *             }
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("data")
    public String getBuilds(
    		@QueryParam("ids") String ids, 
    		@QueryParam("version") String version, 
    		@QueryParam("repo_url") String repoUrl,
    		@QueryParam("branch") String branch,
    		@QueryParam("page") String page,
    		@QueryParam("pagesize") String pagesize) {
    	
    	if(branch!=null && !branch.equals("")){
    		if(repoUrl==null || repoUrl.equals("")){
    			ErrorResponse errorResponse = new ErrorResponse();
        		errorResponse.setCode(ErrorCode.INVALID_PARAMETERS);
        		String message = "if query by repo and branch, both values are required!";
        		errorResponse.setMessage(message);
        		
        		BuildMetadataServiceException exception = new BuildMetadataServiceException(errorResponse.toJson());
        		throw exception;
    		}
    	}
    	
    	Map<String, String> queryParams = new HashMap<String, String>();
    	if(ids!=null && !ids.equals("")) queryParams.put("ids", ids);
    	if(version!=null && !version.equals("")) queryParams.put("version", version);
    	if(repoUrl!=null && !repoUrl.equals("")) queryParams.put("repo_url", repoUrl);
    	if(branch!=null && !branch.equals("")) queryParams.put("branch", branch);
    	if(page!=null && !page.equals("")) queryParams.put("page", page);
    	if(pagesize!=null && !pagesize.equals("")) queryParams.put("pagesize", pagesize);
    	
    	List<Build> builds = buildService.getBuilds(queryParams);
    	
    	HashMap<String, List<Build>> map = new HashMap<String, List<Build>>();
    	map.put("builds", builds);
    	String jsonBuilds = new Gson().toJson(map);
    	return jsonBuilds;
    }
    
    /**
     * curl -v -X DELETE -H "Content-Type: application/json" http://{endpoint}/ws/builds/data/8
     * @param id
     */
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("data/{id}")
    public String deleteBuild(@PathParam("id") String id){
    	if(id==null || id.equals("")){
    		ErrorResponse errorResponse = new ErrorResponse();
    		errorResponse.setCode(ErrorCode.INVALID_PARAMETERS);
    		String message = "id is required for deleting, ex: curl http://{endpoint}/ws/builds/data/8";
    		errorResponse.setMessage(message);
    		
    		BuildMetadataServiceException exception = new BuildMetadataServiceException(errorResponse.toJson());
    		throw exception;
    	}
    	
    	buildService.deleteBuild(Integer.parseInt(id));
    	String message = String.format("Build %s is deleted successfully!", id);
    	SuccessResponse successResponse = new SuccessResponse(message);
    	return new Gson().toJson(successResponse);
    }

    @POST
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public ModelAndView saveBuildForm(@Form BuildForm buildForm)
            throws URISyntaxException {
    	Build build = buildForm.toBuild();
    	buildService.save(build);
        return viewAll();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public ModelAndView viewAll() {
        // forward to the "builds" view, with a request attribute named
        // "builds" that has all of the existing builds
        return new ModelAndView("build/builds", "builds", buildService.getAll());
    }
}
