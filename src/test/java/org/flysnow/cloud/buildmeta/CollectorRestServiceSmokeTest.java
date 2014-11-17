package org.flysnow.cloud.buildmeta;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.domain.model.Collector;
import org.flysnow.cloud.buildmeta.domain.model.CollectorResult;
import org.flysnow.cloud.buildmeta.domain.model.CollectorWithBLOBs;
import org.flysnow.cloud.buildmeta.requests.CreateCollectorResultRequest;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorResponse;
import org.flysnow.cloud.buildmeta.wsclient.BuildMetadataWSClient;
import org.flysnow.cloud.buildmeta.wsclient.BuildWSClientException;
import org.flysnow.cloud.buildmeta.wsclient.CollectorWSClient;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Branch;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CollectorRestServiceSmokeTest {

	private static Logger logger = Logger
			.getLogger(CollectorRestServiceSmokeTest.class);

	private static final String ENDPOINT = "http://localhost:8080/";
	private static final String API_KEY = "devopsadmin";
	private static final String API_SECRET = "devops2014";

	private CollectorWSClient client = new CollectorWSClient(ENDPOINT, API_KEY,
			API_SECRET);

	@Test
	public void testGetByParams() throws Exception {

		logger.info("Get collectors");
		List<Collector> collectors = this.client.getCollectors(
				"test.cloudfarms.net", "bst-devops-buildmeta-64-centos6-1028",
				"71", "text");
		assertTrue(collectors != null);
		for (Collector c : collectors) {
			logger.info("content===" + c.getEnvId());
		}
	}

	// @Test
	// public void testReceive() throws Exception {
	//
	// logger.info("testReceive");
	// CollectorResult r = new CollectorResult();
	// r.setcType("metric");
	// r.setEnv("1");
	// r.setFarm("a");
	// r.setRole("r");
	// r.setServerID("1");
	// r.setTarget("env");
	// r.setText("r");
	// r.setTime(123111111l);
	// CreateCollectorResultRequest c = new CreateCollectorResultRequest();
	// c.setCollectorResult(r);
	// client.postResult(c);
	// }

}
