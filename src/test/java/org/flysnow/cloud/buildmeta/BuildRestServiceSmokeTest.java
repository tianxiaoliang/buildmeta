package org.flysnow.cloud.buildmeta;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorResponse;
import org.flysnow.cloud.buildmeta.wsclient.BuildMetadataWSClient;
import org.flysnow.cloud.buildmeta.wsclient.BuildWSClientException;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Branch;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BuildRestServiceSmokeTest {
	
	private static Logger logger = Logger.getLogger(BuildRestServiceSmokeTest.class);
	
	private static final String ENDPOINT = "http://localhost:8080/";
	private static final String API_KEY="devopsadmin";
	private static final String API_SECRET="devops2014";
	
	private BuildMetadataWSClient buildRegistryWSClient = new BuildMetadataWSClient(ENDPOINT, API_KEY, API_SECRET);
	private Build testBuild;

	@Before
	public void setUp() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String strTime = simpleDateFormat.format(new Date());
		String artifactId = String.format("helloworld-test-%s", strTime);
		String repo = String.format("git@git.flysnow.org:ci/helloworld-test-%s.git", strTime);
		Build voBuild = getTestBuildInfo(artifactId, repo);
		this.testBuild = this.buildRegistryWSClient.createBuildInfo(voBuild);
	}

	@After
	public void tearDown() throws Exception {
		if(this.testBuild!=null){
			this.buildRegistryWSClient.deleteBuild(this.testBuild.getId());
			this.buildRegistryWSClient.deleteBranch(this.testBuild.getRepoUrl(), this.testBuild.getBranch());
			this.buildRegistryWSClient.deleteRepo(this.testBuild.getRepoUrl());
		}
	}
	
	//@Test
	public void testCreateBuildInfoAPI() throws Exception{
		trace(Thread.currentThread().getStackTrace());
		String id = this.testBuild.getId();
		Build build = this.buildRegistryWSClient.getBuild(id);
		assertTrue(build!=null);
		logger.info("build.getVersion()="+build.getVersion());
	    
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    logger.info(gson.toJson(build));
	}
	
	//@Test
	public void testGetBuildsByParams() throws Exception{
		trace(Thread.currentThread().getStackTrace());
		String id = this.testBuild.getId();
		String version = this.testBuild.getVersion();
		String repoUrl = this.testBuild.getRepoUrl();
		String branch = this.testBuild.getBranch();
		
		logger.info("Get builds by ids");
		List<Build> builds = this.buildRegistryWSClient.getBuilds(id, null, null, null);
		assertTrue(builds!=null && builds.size()==1);
		for(Build build : builds){
			logger.info("build.getVersion()="+build.getVersion());
		    
		    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    logger.info(gson.toJson(build));
		}
		
		logger.info("Get builds by version");
		builds = this.buildRegistryWSClient.getBuilds(null, version, null, null);
		assertTrue(builds!=null && builds.size()==1);
		for(Build build : builds){
			logger.info("build.getVersion()="+build.getVersion());
		    
		    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    logger.info(gson.toJson(build));
		}
		
		logger.info("Get builds by repoUrl and branch");
		builds = this.buildRegistryWSClient.getBuilds(null, null, repoUrl, branch);
		assertTrue(builds!=null && builds.size()==1);
		for(Build build : builds){
			logger.info("build.getVersion()="+build.getVersion());
		    
		    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    logger.info(gson.toJson(build));
		}
	}
	
	//@Test
	public void testGetBranches() throws Exception {
		trace(Thread.currentThread().getStackTrace());
		String repoUrl = this.testBuild.getRepoUrl();
		List<Branch> branches = this.buildRegistryWSClient.getBranches(repoUrl);
		assertTrue(branches!=null);
		for(Branch branch : branches){
			logger.info("branch.getName()="+branch.getName());
	    }
	}
	
	//@Test
	public void testGetRepositories() throws Exception {
		trace(Thread.currentThread().getStackTrace());
		List<Repository> repositories = this.buildRegistryWSClient.getRepositories();
		assertTrue(repositories!=null && repositories.size()>=1);
		for(Repository repository : repositories){
			logger.info(String.format("%s %s", repository.getId(), repository.getRepoUrl()));
	    }
	}
		
	//@Test
	public void testDeleteBuild() throws Exception {
		trace(Thread.currentThread().getStackTrace());
		String id = this.testBuild.getId();
		Build build = this.buildRegistryWSClient.getBuild(id);
		assertTrue(build!=null);
		this.buildRegistryWSClient.deleteBuild(id);
		build = this.buildRegistryWSClient.getBuild(id);
		assertTrue(build==null);
	}
	
	//@Test
	public void testDeleteBranch() throws Exception {
		trace(Thread.currentThread().getStackTrace());
		List<Branch> branches = this.buildRegistryWSClient.getBranches(this.testBuild.getRepoUrl());
		
		int beforeDeleteBranchesNum = branches.size();
		logger.info(String.format("There are %s branches of test repo before delete branch", beforeDeleteBranchesNum));
		
		this.buildRegistryWSClient.deleteBranch(this.testBuild.getRepoUrl(), this.testBuild.getBranch());
		
		try {
			this.buildRegistryWSClient.deleteBranch(this.testBuild.getRepoUrl(), null);
		} catch (BuildWSClientException e) {
			String jsonErrorMessage = e.getMessage();
			ErrorResponse errorResponse = new Gson().fromJson(jsonErrorMessage, ErrorResponse.class);
			assertTrue(errorResponse.getCode()==1000);
			logger.error(errorResponse.getMessage());
		}

		branches = this.buildRegistryWSClient.getBranches(this.testBuild.getRepoUrl());
		
		int afterDeleteBranchesNum = branches.size();
		logger.info(String.format("There are %s branches of test repo left after delete branch", afterDeleteBranchesNum));
		assertTrue(branches!=null && branches.size()==0);
	}
	
	//@Test
	public void testDeleteRepository() throws Exception {
		trace(Thread.currentThread().getStackTrace());
		List<Repository> repos = this.buildRegistryWSClient.getRepositories();
		int beforeDeleteRepoNum = repos.size();
		logger.info(String.format("There are %s repos before delete repo", beforeDeleteRepoNum));
		String repoUrl = this.testBuild.getRepoUrl();
		this.buildRegistryWSClient.deleteRepo(repoUrl);
		repos = this.buildRegistryWSClient.getRepositories();
		int afterDeleteRepoNum = repos.size();
		logger.info(String.format("There are %s repos left after delete repo", afterDeleteRepoNum));
		assertTrue((beforeDeleteRepoNum - afterDeleteRepoNum) ==1);
		logger.info("delete repo successfully!");
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

}
