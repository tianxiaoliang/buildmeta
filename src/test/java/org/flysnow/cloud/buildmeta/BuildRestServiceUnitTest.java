package org.flysnow.cloud.buildmeta;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.ui.resteasy.BranchResource;
import org.flysnow.cloud.buildmeta.ui.resteasy.BuildResource;
import org.flysnow.cloud.buildmeta.ui.resteasy.RepositoryResource;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.BuildMetadataServiceException;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Branch;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.CreateBuildInfoRequest;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.CreateBuildInfoResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetBranchResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetBuildInfoResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetRepositoryResponse;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springmvc-servlet.xml")
public class BuildRestServiceUnitTest {
	
	@Autowired
	BuildResource buildResource;
	
	@Autowired
	RepositoryResource repositoryResource;
	
	@Autowired
	BranchResource branchResource;
	
	private Dispatcher dispatcher;
	
	private static Logger logger = Logger.getLogger(BuildRestServiceSmokeTest.class);
	
	private Build voBuild;
	private Build testBuild;

	@Before
	public void setUp() throws Exception {
		this.dispatcher = MockDispatcherFactory.createDispatcher();
	    dispatcher.getRegistry().addSingletonResource(this.repositoryResource);
	    dispatcher.getRegistry().addSingletonResource(this.branchResource);
	    dispatcher.getRegistry().addSingletonResource(this.buildResource);
	    
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String strTime = simpleDateFormat.format(new Date());
		String artifactId = String.format("helloworld-test-%s", strTime);
		String repo = String.format("git@git.flysnow.org:ci/helloworld-test-%s.git", strTime);
		this.voBuild = getTestBuildInfo(artifactId, repo);
	    this.testBuild = this.createTestBuild(voBuild);
	}

	@After
	public void tearDown() throws Exception {
		this.deleteBuild(this.testBuild);
		this.deleteBranch(this.testBuild.getRepoUrl(), this.testBuild.getBranch());
		this.deleteRepository(this.testBuild.getRepoUrl());
	}
	
	private Build createTestBuild(Build voBuild) throws Exception {
		MockHttpRequest request = MockHttpRequest.post("/ws/builds/data");
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
	    
	    CreateBuildInfoRequest createBuildInfoRequest = new CreateBuildInfoRequest();
	    createBuildInfoRequest.setBuild(voBuild);
	    String jsonCreateBuildInfoRequest = new Gson().toJson(createBuildInfoRequest);
	    System.out.println("jsonCreateBuildInfoRequest="+getPrettyJSON(jsonCreateBuildInfoRequest));
	    request.content(jsonCreateBuildInfoRequest.getBytes());
	    
        MockHttpResponse response = new MockHttpResponse();
	
	    dispatcher.invoke(request, response);
	    
	    String jsonCreateBuildInfoResponse = response.getContentAsString();
	    System.out.println("jsonCreateBuildInfoResponse="+getPrettyJSON(jsonCreateBuildInfoResponse));
	    
	    CreateBuildInfoResponse createBuildInfoResponse = new Gson().fromJson(jsonCreateBuildInfoResponse, CreateBuildInfoResponse.class);
	    List<Build> builds = createBuildInfoResponse.getBuilds();
	    Build createdBuild = builds.get(0);
	    Assert.assertEquals(response.getStatus(), 200);
	    Assert.assertEquals(builds.size(), 1);
	    return createdBuild;
	}
	
	private void deleteBuild(Build build) throws Exception {
		MockHttpRequest request = MockHttpRequest.delete("/ws/builds/data/"+build.getId());
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
	    
	    MockHttpResponse response = new MockHttpResponse();
	    dispatcher.invoke(request, response);
	    String jsonResponse = response.getContentAsString();
	    System.out.println("jsonResponse="+getPrettyJSON(jsonResponse));
	}
	
	private void deleteBranch(String repoUrl, String branch) throws Exception {
		MockHttpRequest request = MockHttpRequest.delete("/ws/branch?repo_url="+repoUrl+"&branch="+branch);
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
	    
	    MockHttpResponse response = new MockHttpResponse();
	    dispatcher.invoke(request, response);
	    String jsonResponse = response.getContentAsString();
	    System.out.println("jsonResponse="+getPrettyJSON(jsonResponse));
	}
	
	private void deleteRepository(String repoUrl) throws Exception {
		MockHttpRequest request = MockHttpRequest.delete("/ws/repository?repo_url="+repoUrl);
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
	    
	    MockHttpResponse response = new MockHttpResponse();
	    dispatcher.invoke(request, response);
	    
	    String jsonResponse = response.getContentAsString();
	    System.out.println("jsonResponse="+getPrettyJSON(jsonResponse));
	    
	}
	
	private Build getBuild(String id) throws Exception {
		Build build = null;
		MockHttpRequest request = MockHttpRequest.get("/ws/builds/data?ids="+id);
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
	    MockHttpResponse response = new MockHttpResponse();
	    dispatcher.invoke(request, response);
	    Assert.assertEquals(response.getStatus(), 200);	
	    String content = response.getContentAsString();
	    System.out.println("content="+getPrettyJSON(content));
	    GetBuildInfoResponse getBuildInfoResponse = new Gson().fromJson(content, GetBuildInfoResponse.class);
	    List<Build> builds = getBuildInfoResponse.getBuilds();
	    if(builds!=null && builds.size()>0){
	    	build = builds.get(0);
	    }
	    return build;
	}
	
	private Branch getBranch(String repoUrl, String branchName) throws Exception {
		Branch retBranch = null;
		MockHttpRequest request = MockHttpRequest.get("/ws/branch?repo_url="+repoUrl);
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
	
	    dispatcher.invoke(request, response);
	    
	    Assert.assertEquals(response.getStatus(), 200);
	    String content = response.getContentAsString();
	    GetBranchResponse getBranchResponse = new Gson().fromJson(content, GetBranchResponse.class);
	    List<Branch> branches = getBranchResponse.getBranches();
	    if(branches!=null && branches.size()>0){
	    	for(Branch branch : branches){
	    		if(branch.getName().equals(branchName)){
	    			retBranch = branch;
	    		}
	    	}
	    }
	    return retBranch;
	}
	
	private Repository getRepository(String repoUrl) throws Exception {
		Repository retRepository = null;
		MockHttpRequest request = MockHttpRequest.get("/ws/repository?repo_url="+repoUrl);
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
	
	    dispatcher.invoke(request, response);
	    
	    Assert.assertEquals(response.getStatus(), 200);
	    String content = response.getContentAsString();
	    GetRepositoryResponse getRepositoryResponse = new Gson().fromJson(content, GetRepositoryResponse.class);
	    List<Repository> repositories = getRepositoryResponse.getRepositories();
	    if(repositories!=null && repositories.size()>0){
	    	for(Repository repository : repositories){
	    		if(repository.getRepoUrl().equals(repoUrl)){
	    			retRepository = repository;
	    		}
	    	}
	    }
	    return retRepository;
	}
	
	@Test
	public void testCreateBuildInfoAPI() throws Exception{
	    Assert.assertEquals(this.testBuild.getVersion(), this.voBuild.getVersion());
	}
	
	@Test
	public void testGetBuildsById() throws Exception{
		trace(Thread.currentThread().getStackTrace());
		String id = testBuild.getId();
		String version = testBuild.getVersion();
		String repoUrl = testBuild.getRepoUrl();
		String branch = testBuild.getBranch();
		System.out.println("id="+id);
		
		Build build = this.getBuild(id);
	    assertTrue(build!=null);
	}
	
	@Test
	public void testGetBuildsByVersion() throws Exception{
		trace(Thread.currentThread().getStackTrace());
		String id = testBuild.getId();
		String version = testBuild.getVersion();
		String repoUrl = testBuild.getRepoUrl();
		String branch = testBuild.getBranch();
		System.out.println("id="+id);
		
		MockHttpRequest request = MockHttpRequest.get("/ws/builds/data?version="+version);
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
	    MockHttpResponse response = new MockHttpResponse();
	    dispatcher.invoke(request, response);
	    String content = response.getContentAsString();
	    System.out.println("content="+getPrettyJSON(content));
	    Assert.assertEquals(response.getStatus(), 200);	    
	}
	
	@Test
	public void testGetBuildsByRepoBranch() throws Exception{
		trace(Thread.currentThread().getStackTrace());
		String id = testBuild.getId();
		String version = testBuild.getVersion();
		String repoUrl = testBuild.getRepoUrl();
		String branch = testBuild.getBranch();
		System.out.println("id="+id);
		
		MockHttpRequest request = MockHttpRequest.get("/ws/builds/data?repo_url="+ repoUrl +"&branch="+branch);
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
	    MockHttpResponse response = new MockHttpResponse();
	    dispatcher.invoke(request, response);
	    String content = response.getContentAsString();
	    System.out.println("content="+getPrettyJSON(content));
	    Assert.assertEquals(response.getStatus(), 200);	    
	}
	
	@Test
	public void testGetBuildsByBranchButMissingRepoError() {
		trace(Thread.currentThread().getStackTrace());
		String id = testBuild.getId();
		String version = testBuild.getVersion();
		String repoUrl = testBuild.getRepoUrl();
		String branch = testBuild.getBranch();
		System.out.println("id="+id);
		
		
	    try {
	    	MockHttpRequest request = MockHttpRequest.get("/ws/builds/data?branch="+ branch);
		    request.accept(MediaType.APPLICATION_JSON);
		    request.contentType(MediaType.APPLICATION_JSON);
		    MockHttpResponse response = new MockHttpResponse();
			dispatcher.invoke(request, response);
			Assert.assertEquals(response.getStatus(), 500);
			String content = response.getContentAsString();
		    System.out.println("content="+getPrettyJSON(content));
		} catch (BuildMetadataServiceException e) {
			System.out.println("BuildMetadataServiceException happens");
			System.out.println("errorMessage="+e.getMessage());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Exception happens");
			System.out.println("Exception errorMessage="+e.getMessage());
		}
	    
	    	    
	}
	
	@Test
	public void testGetBranches() throws Exception {
        trace(Thread.currentThread().getStackTrace());
		
		MockHttpRequest request = MockHttpRequest.get("/ws/branch?repo_url="+this.testBuild.getRepoUrl());
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
	
	    dispatcher.invoke(request, response);
	    
	    Assert.assertEquals(response.getStatus(), 200);
	    String content = response.getContentAsString();
	    GetBranchResponse getBranchResponse = new Gson().fromJson(content, GetBranchResponse.class);
	    List<Branch> branches = getBranchResponse.getBranches();
	    assertTrue(branches!=null && branches.size()>=1);
	}
	
	@Test
	public void testGetRepositories() throws Exception {
		trace(Thread.currentThread().getStackTrace());
		
		MockHttpRequest request = MockHttpRequest.get("/ws/repository");
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();
	
	    dispatcher.invoke(request, response);
	    
	    String content = response.getContentAsString();
	    System.out.println("content="+content);
	    GetRepositoryResponse getRepositoryResponse = new Gson().fromJson(content, GetRepositoryResponse.class);
	    Assert.assertEquals(response.getStatus(), 200);
	    List<Repository> repos = getRepositoryResponse.getRepositories();
	    assertTrue(repos!=null && repos.size()>=1);
	}
		
	@Test
	public void testDeleteBuild() throws Exception {
		MockHttpRequest request = MockHttpRequest.delete("/ws/builds/data/"+this.testBuild.getId());
	    request.accept(MediaType.APPLICATION_JSON);
	    request.contentType(MediaType.APPLICATION_JSON);
	    
	    MockHttpResponse response = new MockHttpResponse();
	    dispatcher.invoke(request, response);
	    //Assert.assertEquals(response.getStatus(), 200);
	    
	    Build build = getBuild(this.testBuild.getId());
	    assertTrue(build==null);
	}
	
	@Test
	public void testDeleteBranch() throws Exception {
		this.deleteBranch(this.testBuild.getRepoUrl(), this.testBuild.getBranch());
		Branch branch = this.getBranch(this.testBuild.getRepoUrl(), this.testBuild.getBranch());
		assertTrue(branch==null);
	}
	
	@Test
	public void testDeleteRepository() throws Exception {
		this.deleteRepository(this.testBuild.getRepoUrl());
		
		Repository repository = getRepository(this.testBuild.getRepoUrl());
		assertTrue(repository==null);
	}
	
	private Build getTestBuildInfo(String strArtifactId, String repoUrl){
        Build build = new Build();
		
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String strTime = simpleDateFormat.format(new Date());
		
		//String strArtifactId = "helloworld";
		String strBranch = String.format("master-%s", strTime);
		String groupId = "org.flysnow.cloud";
		String md5 = "a935373d6cd982b090a0de51adf25943";
		String packaging = "zip";
		//String repoUrl = "git@git.flysnow.org:ci/helloworld.git";
		String type = "continuous";
		String version = String.format("0.2-%s-%s", strTime, System.currentTimeMillis());
		String downloadUrl = String.format("https://www.buildstore.com/nexus/content/repositories/snapshots/com/flysnow/%s/0.1-SNAPSHOT/%s-%s.zip",
				strArtifactId, strArtifactId, version);
		
		build.setArtifactId(strArtifactId);
		build.setBranch(strBranch);
		build.setDownloadUrl(downloadUrl);
		build.setGroupId(groupId);
        build.setMd5(md5);
        build.setPackaging(packaging);
        build.setRepoUrl(repoUrl);
        build.setType(type);
        build.setVersion(version);
        return build;
	}
	
	public static void trace(StackTraceElement e[]) {
		boolean doNext = false;
		for (StackTraceElement s : e) {
			if (doNext) {
				logger.info("");
				logger.info("-------------------------------------");
				logger.info(s.getMethodName());
				logger.info("-------------------------------------");
				return;
			}
			doNext = s.getMethodName().equals("getStackTrace");
		}
	}
	
	private static String getPrettyJSON(String jsonStr){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonStr);
		String prettyJsonString = gson.toJson(je);
		return prettyJsonString;
	}

}
