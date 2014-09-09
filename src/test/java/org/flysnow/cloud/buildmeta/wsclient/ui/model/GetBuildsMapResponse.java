package org.flysnow.cloud.buildmeta.wsclient.ui.model;

import java.util.Map;

import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;

public class GetBuildsMapResponse {
    
	private Map<String, Build> mapBuilds;

	public Map<String, Build> getMapBuilds() {
		return mapBuilds;
	}

	public void setMapBuilds(Map<String, Build> mapBuilds) {
		this.mapBuilds = mapBuilds;
	}

}
