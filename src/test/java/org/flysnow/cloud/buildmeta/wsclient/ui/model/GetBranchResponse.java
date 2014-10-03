package org.flysnow.cloud.buildmeta.wsclient.ui.model;

import java.util.List;

import org.flysnow.cloud.buildmeta.wsclient.domain.model.Branch;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;

public class GetBranchResponse {
    
	private List<Branch> branches;

	public List<Branch> getBranches() {
		return branches;
	}

	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}
	
}
