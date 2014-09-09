package org.flysnow.cloud.buildmeta.wsclient.ui.model;

import java.util.List;

import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;

public class CreateBuildInfoResponse {
    
	private List<Build> builds;

	public List<Build> getBuilds() {
		return builds;
	}

	public void setBuilds(List<Build> builds) {
		this.builds = builds;
	}
	
}
