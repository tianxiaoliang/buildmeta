package org.flysnow.cloud.buildmeta.ui.resteasy.form;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

import org.flysnow.cloud.buildmeta.domain.model.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@XmlRootElement(name="build")
public class BuildForm {
	private String id;
	private String repoUrl;
	private String branch;
	private String groupId;
	private String artifactId;
	private String packaging;
	private String type;
	private String version;
	private String md5;
	private String downloadUrl;
	private String classifier;

	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getRepoUrl() {
		return repoUrl;
	}

	@FormParam("repoUrl")
	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public String getBranch() {
		return branch;
	}

	@FormParam("branch")
	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getGroupId() {
		return groupId;
	}

	@FormParam("groupId")
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	@FormParam("artifactId")
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getPackaging() {
		return packaging;
	}

	@FormParam("packaging")
	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public String getType() {
		return type;
	}

	@FormParam("type")
	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	@FormParam("version")
	public void setVersion(String version) {
		this.version = version;
	}

	public String getMd5() {
		return md5;
	}

	@FormParam("md5")
	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	@FormParam("downloadUrl")
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getClassifier() {
		return classifier;
	}

	@FormParam("classifier")
	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}
	
	public boolean equals(Object other) {
        // normal checks apply here...
        BuildForm otherBuild = (BuildForm) other;
        return otherBuild.repoUrl.equals(this.repoUrl)
        		&& otherBuild.branch.equals(this.branch)
                && otherBuild.artifactId.equals(this.artifactId)
                && otherBuild.version.equals(this.version)
                && otherBuild.md5.equals(this.md5)
                && otherBuild.downloadUrl.equals(this.downloadUrl);
    }

    public int hashCode() {
        return md5.hashCode() ^ downloadUrl.hashCode();
    }
    
    public static BuildForm fromBuild(Build build){
    	BuildForm buildForm = new BuildForm();
    	buildForm.setId(String.valueOf(build.getId()));
    	buildForm.setArtifactId(build.getArtifactId());
    	buildForm.setBranch(build.getBranch());
    	buildForm.setMd5(build.getMd5());
    	buildForm.setVersion(build.getVersion());
    	buildForm.setType(build.getType());
    	buildForm.setClassifier(build.getClassifier());
    	buildForm.setDownloadUrl(build.getDownloadUrl());
    	buildForm.setRepoUrl(build.getRepoUrl());
    	buildForm.setGroupId(build.getGroupId());
    	buildForm.setPackaging(build.getPackaging());
    	return buildForm;
    }
    
    public Build toBuild(){
    	Build build = new Build();
    	if(this.id!=null){
    		build.setId(Integer.valueOf(this.id));
    	}else{
    		build.setId(0);
    	}
    	
    	build.setRepoUrl(this.repoUrl);
    	build.setBranch(this.branch);
    	build.setVersion(this.version);
    	build.setType(this.type);
    	build.setGroupId(this.groupId);
    	build.setArtifactId(this.artifactId);
    	build.setPackaging(this.packaging);
    	build.setClassifier(this.classifier);
    	build.setMd5(this.md5);
    	build.setDownloadUrl(this.downloadUrl);
    	build.setCreated(System.currentTimeMillis());
    	return build;
    }
    
    public String toJSON(){
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	return gson.toJson(this);
    }
}
