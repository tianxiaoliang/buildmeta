package org.flysnow.cloud.buildmeta.wsclient.domain.model;

public class Repository {
	private Integer id;
	private String repoUrl;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRepoUrl() {
		return repoUrl;
	}
	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}
}
