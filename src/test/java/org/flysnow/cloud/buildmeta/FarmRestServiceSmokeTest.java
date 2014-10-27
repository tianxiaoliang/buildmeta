package org.flysnow.cloud.buildmeta;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.domain.model.Collector;
import org.flysnow.cloud.buildmeta.domain.model.CollectorResult;
import org.flysnow.cloud.buildmeta.domain.model.Farm;
import org.flysnow.cloud.buildmeta.requests.CreateCollectorResultRequest;
import org.flysnow.cloud.buildmeta.requests.RegisterFarmResultRequest;
import org.flysnow.cloud.buildmeta.ui.resteasy.exception.ErrorResponse;
import org.flysnow.cloud.buildmeta.wsclient.BuildMetadataWSClient;
import org.flysnow.cloud.buildmeta.wsclient.BuildWSClientException;
import org.flysnow.cloud.buildmeta.wsclient.CollectorWSClient;
import org.flysnow.cloud.buildmeta.wsclient.FarmWSClient;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Branch;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FarmRestServiceSmokeTest {

	private static Logger logger = Logger
			.getLogger(FarmRestServiceSmokeTest.class);

	private static final String ENDPOINT = "http://localhost:8080/";
	private static final String API_KEY = "devopsadmin";
	private static final String API_SECRET = "devops2014";

	private FarmWSClient client = new FarmWSClient(ENDPOINT, API_KEY,
			API_SECRET);

	@Test
	public void testRegister() throws Exception {

		logger.info("testRegister");
		Farm farm = new Farm();
		farm.setAccountId(9);
		farm.setScalrEndpoint("test.cloudfarms.net");
		farm.setScalrEnvId("71");
		farm.setScalrEnvName("bst-dev");
		farm.setScalrFarmId("1041");
		farm.setScalrFarmName("bst-devops-production1");
		farm.setSystemName("devops");
		farm.setSystemType("devops");
		farm.setFarmType("dev");
		RegisterFarmResultRequest r = new RegisterFarmResultRequest();
		r.setFarm(farm);
		client.register(r);
	}

	@Test
	public void testGetByParams() throws Exception {

		logger.info("Get farms");
		String res = this.client.getFarms("test.cloudfarms.net");
		System.out.println(res);
	}

}
