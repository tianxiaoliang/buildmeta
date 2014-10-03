package org.flysnow.cloud.buildmeta.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.domain.dao.BranchMapper;
import org.flysnow.cloud.buildmeta.domain.dao.BuildMapper;
import org.flysnow.cloud.buildmeta.domain.dao.RepositoryMapper;
import org.flysnow.cloud.buildmeta.domain.model.Branch;
import org.flysnow.cloud.buildmeta.domain.model.BranchExample;
import org.flysnow.cloud.buildmeta.domain.model.Build;
import org.flysnow.cloud.buildmeta.domain.model.BuildExample;
import org.flysnow.cloud.buildmeta.domain.model.Repository;
import org.flysnow.cloud.buildmeta.domain.model.RepositoryExample;
import org.flysnow.cloud.buildmeta.domain.model.BuildExample.Criteria;
import org.flysnow.cloud.buildmeta.ui.resteasy.BuildResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BuildService {
	private Logger logger = Logger.getLogger(BuildResource.class);
	
    @Autowired
    BuildMapper buildMapper;
    
    @Autowired
    BranchMapper branchMapper;
    
    @Autowired
    RepositoryMapper repositoryMapper;
    
    public void save(Build build) {
    	//first check if exist
    	BuildExample buildExample = new BuildExample();
    	org.flysnow.cloud.buildmeta.domain.model.BuildExample.Criteria criteria = buildExample.createCriteria();
    	criteria.andArtifactIdEqualTo(build.getArtifactId());
    	criteria.andVersionEqualTo(build.getVersion());
    	criteria.andBranchEqualTo(build.getBranch());
    	criteria.andGroupIdEqualTo(build.getGroupId());
    	criteria.andRepoUrlEqualTo(build.getRepoUrl());
    	List<Build> builds = buildMapper.selectByExample(buildExample);
    	if(builds==null || builds.size()==0){
    		buildMapper.insert(build);
    		logger.debug(String.format("Save build %s successfully!", build.getVersion()));
    	}else{
    		logger.info(String.format("build %s %s already exists on repo %s", 
    				build.getArtifactId(), build.getVersion(),
    				build.getRepoUrl()));
    	}
    }
    
    public void deleteBuild(Integer id) {
    	buildMapper.deleteByPrimaryKey(id);
    }
    
    public void deleteBranch(String repo, String branch) {
    	
    	BranchExample branchExample = new BranchExample();
    	org.flysnow.cloud.buildmeta.domain.model.BranchExample.Criteria criteria = branchExample.createCriteria();
    	criteria.andRepoUrlEqualTo(repo);
    	criteria.andNameEqualTo(branch);
    	branchMapper.deleteByExample(branchExample);
    	
    }
    
    public void deleteRepo(String repo) {
    	RepositoryExample repositoryExample = new RepositoryExample();
    	org.flysnow.cloud.buildmeta.domain.model.RepositoryExample.Criteria criteria = repositoryExample.createCriteria();
    	criteria.andRepoUrlEqualTo(repo);
    	
    	repositoryMapper.deleteByExample(repositoryExample);
    }
    
    public void saveBranch(Branch branch){
    	//first check if exist
    	BranchExample branchExample = new BranchExample();
    	org.flysnow.cloud.buildmeta.domain.model.BranchExample.Criteria criteria = branchExample.createCriteria();
    	criteria.andRepoUrlEqualTo(branch.getRepoUrl());
    	criteria.andNameEqualTo(branch.getName());
    	List<Branch> branches = branchMapper.selectByExample(branchExample);
    	if(branches==null || branches.size()==0){
    		//if not exist save
    		logger.info(String.format("branch %s does not exist, repo is %s", branch.getName(),
    				branch.getRepoUrl()));
    		logger.info("branch.getRepoUrl()="+branch.getRepoUrl());
    		logger.info("branch.getName()="+branch.getName());
    		branchMapper.insert(branch);
    	}else{
    		logger.info(String.format("branch %s already exists on repo %s", branch.getName(),
    				branch.getRepoUrl()));
    	}
    }
    
    public void saveRepo(Repository repo){
    	RepositoryExample repoExample = new RepositoryExample();
    	org.flysnow.cloud.buildmeta.domain.model.RepositoryExample.Criteria criteria = repoExample.createCriteria();
    	criteria.andRepoUrlEqualTo(repo.getRepoUrl());
    	List<Repository> repos = repositoryMapper.selectByExample(repoExample);
    	if(repos==null || repos.size()==0){
    		repositoryMapper.insert(repo);
    		logger.info(String.format("New repository %s created", repo.getRepoUrl()));
    	}else{
    		logger.info(String.format("Repository %s already exists", repo.getRepoUrl()));
    	}
    }
    
    public List<Repository> getRepos(){
    	List<Repository> repos = repositoryMapper.selectByExample(null);
    	return repos;
    }
    
    public List<Branch> getBranches(String repoUrl){
    	BranchExample branchExample = new BranchExample();
    	org.flysnow.cloud.buildmeta.domain.model.BranchExample.Criteria criteria = branchExample.createCriteria();
    	criteria.andRepoUrlEqualTo(repoUrl);
    	List<Branch> branches = branchMapper.selectByExample(branchExample);
    	return branches;
    }
    
    public List<Build> getBuilds(Map<String, String> queryParams){
    	BuildExample example = new BuildExample();
    	org.flysnow.cloud.buildmeta.domain.model.BuildExample.Criteria criteria = example.createCriteria();
    	String strIds = queryParams.get("ids");
    	if(strIds!=null){
    		List<Integer> listIds = new ArrayList<Integer>();
    		if(strIds!=null){
        		String[] strIdsArray = strIds.split(",");
        		if(strIdsArray!=null && strIdsArray.length>0){
        			for(String strId : strIdsArray){
        				listIds.add(Integer.parseInt(strId));
        			}
        		}
        	}
    		criteria.andIdIn(listIds);
    	}
    	
    	String version = queryParams.get("version");
    	if(version!=null){
    		criteria.andVersionEqualTo(version);
    	}
    	
    	String repoUrl = queryParams.get("repo_url");
    	if(repoUrl!=null){
    		criteria.andRepoUrlEqualTo(repoUrl);
    	}
    	
    	String branch = queryParams.get("branch");
    	if(branch!=null){
    		criteria.andBranchEqualTo(branch);
    	}
    	    	
    	List<Build> builds = buildMapper.selectByExample(example);
        return builds;
    	
    }

    public Build getBuild(int id) {
    	BuildExample example = new BuildExample();
    	org.flysnow.cloud.buildmeta.domain.model.BuildExample.Criteria criteria = example.createCriteria();
    	criteria.andIdEqualTo(new Integer(id));
    	List<Build> builds = buildMapper.selectByExample(example);
    	Build build = null;
    	if(builds!=null && builds.size()>0)
    	    build = builds.get(0);
        return build;
    }
    
    public List<Build> getBuildByIds(List<Integer> ids){
    	BuildExample example = new BuildExample();
    	org.flysnow.cloud.buildmeta.domain.model.BuildExample.Criteria criteria = example.createCriteria();
    	criteria.andIdIn(ids);
    	List<Build> builds = buildMapper.selectByExample(example);
        return builds;
    }
    
    public List<Build> getBuildByVersion(String version){
    	BuildExample example = new BuildExample();
    	org.flysnow.cloud.buildmeta.domain.model.BuildExample.Criteria criteria = example.createCriteria();
    	criteria.andVersionEqualTo(version);
    	List<Build> builds = buildMapper.selectByExample(example);
        return builds;
    }
    
    public List<Build> getBranchBuilds(String repoUrl, String branch){
    	BuildExample example = new BuildExample();
    	org.flysnow.cloud.buildmeta.domain.model.BuildExample.Criteria buildCriteria = example.createCriteria();
    	buildCriteria.andRepoUrlEqualTo(repoUrl);
    	buildCriteria.andBranchEqualTo(branch);
    	List<Build> builds = buildMapper.selectByExample(example);
    	return builds;
    }
    
    public List<Build> getAll(){
    	BuildExample example = new BuildExample();
    	Criteria buildCriteria = example.createCriteria();
    	buildCriteria.andIdGreaterThan(0);
    	List<Build> builds = buildMapper.selectByExample(example);
    	return builds;
    }

}
