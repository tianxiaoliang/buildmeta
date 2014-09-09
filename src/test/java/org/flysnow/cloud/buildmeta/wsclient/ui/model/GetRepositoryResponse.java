package org.flysnow.cloud.buildmeta.wsclient.ui.model;

import java.util.List;

import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;

public class GetRepositoryResponse {
    
	private List<Repository> repositories;

	public List<Repository> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<Repository> repositories) {
		this.repositories = repositories;
	}
	
}
