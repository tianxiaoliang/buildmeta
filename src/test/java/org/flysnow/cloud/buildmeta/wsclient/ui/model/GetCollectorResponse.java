package org.flysnow.cloud.buildmeta.wsclient.ui.model;

import java.util.List;

import org.flysnow.cloud.buildmeta.domain.model.Collector;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Branch;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;

public class GetCollectorResponse {

	private List<Collector> collectors;

	public List<Collector> getCollectors() {
		return collectors;
	}

	public void setCollectors(List<Collector> collectors) {
		this.collectors = collectors;
	}

}
