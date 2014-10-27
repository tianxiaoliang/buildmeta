package org.flysnow.cloud.buildmeta.requests;

import org.flysnow.cloud.buildmeta.domain.model.CollectorResult;
import org.flysnow.cloud.buildmeta.domain.model.Farm;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;

public class RegisterFarmResultRequest {

	private Farm farm;

	public Farm getFarm() {
		return farm;
	}

	public void setFarm(Farm farm) {
		this.farm = farm;
	}

}
