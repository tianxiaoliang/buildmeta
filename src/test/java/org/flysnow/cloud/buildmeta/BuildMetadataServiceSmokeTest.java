package org.flysnow.cloud.buildmeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.flysnow.cloud.buildmeta.wsclient.BuildMetadataWSClient;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Branch;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BuildMetadataServiceSmokeTest extends TestCase {
	
	private static final String endpoint = "http://localhost:8080/";
	private static final String apiKey="devopsadmin";
	private static final String apiSecret="devops2014";
	
	private BuildMetadataWSClient buildRegistryWSClient = new BuildMetadataWSClient(endpoint, apiKey, apiSecret);
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void printResultMessage(String strMessage){
		System.out.println("      " + strMessage );
	}
	
	@Test
	public void testWSAPIs() throws Exception {
		//1. Create Build
		System.out.println("Create Build Info... ...");
		System.out.println("----------------------------------------------------");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String strTime = simpleDateFormat.format(new Date());
		String artifactId = String.format("helloworld-test-%s", strTime);
		String repo = String.format("git@git.flysnow.org:ci/helloworld-test-%s.git", strTime);
		
		Build voBuild = getTestBuildInfo(artifactId, repo);
		
		Build entityBuild = this.buildRegistryWSClient.createBuildInfo(voBuild);
	    assertTrue(entityBuild!=null);
	    printResultMessage("Create build info successfully!");
	    
	    System.out.println(entityBuild.getId());
	    System.out.println(entityBuild.getVersion());
	    
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(entityBuild));
		
        //2. Get Build By Version
		System.out.println("Get Build By Version... ...");
		System.out.println("----------------------------------------------------");
		Build buildGetByVersion = this.buildRegistryWSClient.getBuildByVersion(entityBuild.getVersion());
		assertTrue(buildGetByVersion!=null);
		assertTrue(buildGetByVersion.getId().equals(entityBuild.getId()));
		assertTrue(buildGetByVersion.getVersion().equals(entityBuild.getVersion()));
		printResultMessage("Get build by version successfully!");
	    
		//3. Get Build By Id
		System.out.println("Get Build By Id... ...");
		System.out.println("----------------------------------------------------");
		Build buildGetById = this.buildRegistryWSClient.getBuild(entityBuild.getId());
		assertTrue(buildGetById!=null);
		assertTrue(buildGetById.getId().equals(entityBuild.getId()));
		assertTrue(buildGetById.getVersion().equals(entityBuild.getVersion()));
		printResultMessage("Get build by id successfully!");
		
		//4. Get branch builds
		System.out.println("Get Build By branch... ...");
		System.out.println("----------------------------------------------------");
		List<Build> buildsGetByBranch = this.buildRegistryWSClient.getBuilds(entityBuild.getRepoUrl(), entityBuild.getBranch());
		assertTrue(buildsGetByBranch!=null);
		Build buildGetByBranch = buildsGetByBranch.get(0);
		assertTrue(buildGetByBranch!=null);
		assertTrue(buildGetByBranch.getId().equals(entityBuild.getId()));
		assertTrue(buildGetByBranch.getVersion().equals(entityBuild.getVersion()));
		printResultMessage("Get builds of branch successfully!");
		
		//5. Get Branches
		System.out.println("Get branches of repo... ...");
		System.out.println("----------------------------------------------------");
		List<Branch> branches = this.buildRegistryWSClient.getBranches(entityBuild.getRepoUrl());
		assertTrue(branches!=null && branches.size()==1);
		assertTrue(branches.get(0).getName().equals(entityBuild.getBranch()));
		printResultMessage("Get branches of repo successfully!");
        
		//6. Get Repos
		System.out.println("Get repos... ...");
		System.out.println("----------------------------------------------------");
		List<Repository> repos = this.buildRegistryWSClient.getRepositories();
		assertTrue(repos!=null);
		boolean repoExists = false;
		for(Repository repository : repos){
			if(repository.getRepoUrl().equals(entityBuild.getRepoUrl())){
				repoExists = true;
			}
	    } 
		assertTrue(repoExists);
		printResultMessage("Get repo successfully!");
		
		//7. Get Builds by ids
		System.out.println("Get builds by ids... ...");
		System.out.println("----------------------------------------------------");
		String ids = entityBuild.getId();
		List<Build> buildsGetByIds = this.buildRegistryWSClient.getBuilds(ids);
		assertTrue(buildsGetByIds!=null && buildsGetByIds.size()==1);
		printResultMessage("Get builds by ids successfully!");
		
		//9. Delete build just created
		System.out.println("Delete build created... ...");
		System.out.println("----------------------------------------------------");
		this.buildRegistryWSClient.deleteBuild(entityBuild.getId());
		printResultMessage("Delete build successfully!");
		
		//10. Delete branch just created
		System.out.println("Delete branch created... ...");
		System.out.println("----------------------------------------------------");
		this.buildRegistryWSClient.deleteBranch(entityBuild.getRepoUrl(), entityBuild.getBranch());
		printResultMessage("Delete branch successfully!");
		
		//11. Delete repo just created
		System.out.println("Delete repo created... ...");
		System.out.println("----------------------------------------------------");
		this.buildRegistryWSClient.deleteRepo(entityBuild.getRepoUrl());
		printResultMessage("Delete repo successfully!");
	}
	
	private Build getTestBuildInfo(String strArtifactId, String repoUrl){
        Build build = new Build();
		
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String strTime = simpleDateFormat.format(new Date());
		
		//String strArtifactId = "helloworld";
		String strBranch = String.format("master-%s", strTime);
		String groupId = "com.flysnow.cloud";
		String md5 = "a935373d6cd982b090a0de51adf25943";
		String packaging = "zip";
		//String repoUrl = "git@git.flysnow.org:ci/helloworld.git";
		String type = "continuous";
		String version = String.format("0.2-%s-%s", strTime, System.currentTimeMillis());
		String downloadUrl = String.format("https://www.flysnow.com/nexus/content/repositories/snapshots/com/flysnow/%s/0.1-SNAPSHOT/%s-%s.zip",
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
	
//	public static TestSuite getTestsuite() { 
//        TestSuite suite = new TestSuite("Test for test"); 
//        //$JUnit-BEGIN$ 
//        suite.addTestSuite(BuildMetadataServiceSmokeTest.class); 
//        //$JUnit-END$ 
//        return suite; 
//    } 
//	
//	public void main(String[] args){
//		TestSuite testSuite = getTestsuite();
//		junit.textui.TestRunner.run(testSuite);
//	}

}
