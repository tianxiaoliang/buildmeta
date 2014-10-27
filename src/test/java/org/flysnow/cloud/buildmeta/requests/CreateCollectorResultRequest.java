package org.flysnow.cloud.buildmeta.requests;

import org.flysnow.cloud.buildmeta.domain.model.CollectorResult;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;

public class CreateCollectorResultRequest {
    
	private CollectorResult collectorResult;

	public CollectorResult getCollectorResult() {
		return collectorResult;
	}

	public void setCollectorResult(CollectorResult collectorResult) {
		this.collectorResult = collectorResult;
	}

	 
	
}
