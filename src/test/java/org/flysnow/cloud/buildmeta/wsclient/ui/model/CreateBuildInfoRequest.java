package org.flysnow.cloud.buildmeta.wsclient.ui.model;

import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;

public class CreateBuildInfoRequest {
    
	private Build build;

	public Build getBuild() {
		return build;
	}

	public void setBuild(Build build) {
		this.build = build;
	}
	
}
