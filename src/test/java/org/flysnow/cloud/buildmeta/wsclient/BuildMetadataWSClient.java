package org.flysnow.cloud.buildmeta.wsclient;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Branch;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.CreateBuildInfoRequest;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.CreateBuildInfoResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetBranchResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetBuildInfoResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetRepositoryResponse;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class BuildMetadataWSClient {
	private Logger logger = Logger.getLogger(BuildMetadataWSClient.class);
	
	private static final boolean DEBUG = false;
    
	private String endpoint;
	private String apiKey;
	private String apiSecret;
	private OAuthService oAuthService;
	
	private static String BUILD_URI = "/ws/builds/data";
	private static String REPO_URI = "/ws/repository";
	private static String BRANCH_URI = "/ws/branch";
	
	public BuildMetadataWSClient(String endpoint, String apiKey, String apiSecret){
		if(endpoint.endsWith("/")) 
			endpoint = endpoint.substring(0, endpoint.length()-1);
		this.endpoint = endpoint;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		if(DEBUG){
			this.oAuthService = new ServiceBuilder().provider(DummyAPIProvider.class).debug().apiKey(this.apiKey)
					   .apiSecret(this.apiSecret)
					   .build();
		}else {
			this.oAuthService = new ServiceBuilder().provider(DummyAPIProvider.class).apiKey(this.apiKey)
					   .apiSecret(this.apiSecret)
					   .build();
		}
	}
	
	public void deleteBranch(String repoUrl, String branch) throws BuildWSClientException{
        OAuthRequest request = new OAuthRequest(Verb.DELETE, this.endpoint + BRANCH_URI);
        if(repoUrl!=null)
		    request.addQuerystringParameter("repo_url", repoUrl);
		if(branch!=null)
		    request.addQuerystringParameter("branch", branch);
		
		this.sendRequest(request);
	}
	
    public void deleteRepo(String repoUrl) throws BuildWSClientException {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, this.endpoint + REPO_URI);
		request.addQuerystringParameter("repo_url", repoUrl);
		
		this.sendRequest(request);
	}
	
	public void deleteBuild(String id) throws BuildWSClientException {
        String strURI = BUILD_URI + "/" + id;
        OAuthRequest request = new OAuthRequest(Verb.DELETE, this.endpoint + strURI);
		this.sendRequest(request);
	}
	
	public List<Branch> getBranches(String repoUrl) throws BuildWSClientException {
		List<Branch> branches = null;
		
		OAuthRequest request = new OAuthRequest(Verb.GET, this.endpoint + BRANCH_URI);
		request.addQuerystringParameter("repo_url", repoUrl);
		
		String jsonResponse = this.sendRequest(request);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		GetBranchResponse getBranchResponse = gson.fromJson(jsonResponse, GetBranchResponse.class);
		branches = getBranchResponse.getBranches();
		
		return branches;
	}
	
	public List<Repository> getRepositories() throws BuildWSClientException {
		List<Repository> repos = null;
		
		OAuthRequest request = new OAuthRequest(Verb.GET, this.endpoint + REPO_URI);
		String jsonResponse = this.sendRequest(request);
			
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		GetRepositoryResponse getReposResponse = gson.fromJson(jsonResponse, GetRepositoryResponse.class);
		repos = getReposResponse.getRepositories();
		
		return repos;
	}
		
	public List<Build> getBuilds(String ids, String version, String repoUrl, String branch) throws BuildWSClientException {
		List<Build> builds = null;
		
		OAuthRequest request = new OAuthRequest(Verb.GET, this.endpoint + BUILD_URI);
		if(ids!=null) request.addQuerystringParameter("ids", ids);
		if(version!=null) request.addQuerystringParameter("version", version);
		if(repoUrl!=null) request.addQuerystringParameter("repo_url", repoUrl);
		if(branch!=null) request.addQuerystringParameter("branch", branch);
		
		String jsonResponse = this.sendRequest(request);
			
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		GetBuildInfoResponse getBuildInfoResponse = gson.fromJson(jsonResponse, GetBuildInfoResponse.class);
	    builds = getBuildInfoResponse.getBuilds();
		
		return builds;
	}
	
	public Build getBuild(String id) throws BuildWSClientException {
		Build build = null;
		List<Build> builds = getBuilds(id, null, null, null);
		if(builds!=null && builds.size()>0){
			build = builds.get(0);
		}
		return build;
	}
	
	public Build getBuildByVersion(String version) throws BuildWSClientException {
		Build build = null;
		List<Build> builds = getBuilds(null, version, null, null);
		if(builds!=null && builds.size()>0){
			build = builds.get(0);
		}
		return build;
	}
	
	public List<Build> getBuilds(String ids) throws BuildWSClientException {
		List<Build> builds = getBuilds(ids, null, null, null);
		return builds;
	}
	
	public List<Build> getBuilds(String repoUrl, String branch) throws BuildWSClientException {
		List<Build> builds = getBuilds(null, null, repoUrl, branch);
		return builds;
	}
	
	public Build createBuildInfo(Build build) throws BuildWSClientException {
		CreateBuildInfoRequest createBuildInfoRequest = new CreateBuildInfoRequest();
		createBuildInfoRequest.setBuild(build);
		
		GsonBuilder gb = new GsonBuilder().setPrettyPrinting();
		Gson gson = gb.create();
		
		String strRegisterBuildInfoRequest = gson.toJson(createBuildInfoRequest);
		
		OAuthRequest request = new OAuthRequest(Verb.POST, this.endpoint + BUILD_URI);
		request.addPayload(strRegisterBuildInfoRequest);
		
		String jsonResponse = this.sendRequest(request);
		CreateBuildInfoResponse createBuildInfoResponse =  gson.fromJson(jsonResponse, CreateBuildInfoResponse.class);
		if(createBuildInfoResponse!=null){
			List<Build> builds = createBuildInfoResponse.getBuilds();
			if(builds!=null && builds.size()>0){
				build = builds.get(0);
			}
		}
		return build;
	}
	
	public String sendRequest(OAuthRequest request) throws BuildWSClientException {
		request.addHeader("Content-Type", "application/json");
		
		Map<String, String> headers = request.getHeaders();
		
		if(DEBUG) logger.debug("Request Headers:");
		if(DEBUG) logger.debug("---------------------------------------");
		for(Map.Entry<String, String> entry: headers.entrySet()){
			if(DEBUG) logger.debug("    " + entry.getKey()+"="+entry.getValue());
		}
		
		Token accessToken = new Token("", "");
		oAuthService.signRequest(accessToken, request);
		Response response = request.send();
		
		int code = response.getCode();
		logger.info("code=" + code);
		//String strMsg = response.getMessage();
		String jsonResponse = response.getBody();
		logger.info("jsonResponse=" + formatJSON(jsonResponse));
		
		//print response json pretty
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonResponse);
		String prettyJsonString = gson.toJson(je);
		if(DEBUG) logger.debug("\n"+prettyJsonString);
		
		if(code>=300){
			BuildWSClientException exception = new BuildWSClientException(jsonResponse);
			throw exception;
		}

		return jsonResponse;
	}
	
	private static String formatJSON(String jsonStr){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonStr);
		String prettyJsonString = gson.toJson(je);
		return prettyJsonString;
	}
}
