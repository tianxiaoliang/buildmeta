package org.flysnow.cloud.buildmeta.wsclient;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.domain.model.Collector;
import org.flysnow.cloud.buildmeta.domain.model.CollectorResult;
import org.flysnow.cloud.buildmeta.requests.CreateCollectorResultRequest;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Branch;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Build;
import org.flysnow.cloud.buildmeta.wsclient.domain.model.Repository;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.CreateBuildInfoRequest;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.CreateBuildInfoResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetBranchResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetBuildInfoResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetCollectorResponse;
import org.flysnow.cloud.buildmeta.wsclient.ui.model.GetRepositoryResponse;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CollectorWSClient {
	private Logger logger = Logger.getLogger(CollectorWSClient.class);

	private static final boolean DEBUG = false;

	private String endpoint;
	private String apiKey;
	private String apiSecret;
	private OAuthService oAuthService;
	static Token accessToken = new Token("rtcIe1nbtwhG8fjfSPnUiIM4hWegrWGNGeRokHdI", "");
	private static String URI = "/ws/collector";

	public CollectorWSClient(String endpoint, String apiKey, String apiSecret) {
		if (endpoint.endsWith("/"))
			endpoint = endpoint.substring(0, endpoint.length() - 1);
		this.endpoint = endpoint;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		if (DEBUG) {
			this.oAuthService = new ServiceBuilder()
					.provider(DummyAPIProvider.class).debug()
					.apiKey(this.apiKey).apiSecret(this.apiSecret).build();
		} else {
			this.oAuthService = new ServiceBuilder()
					.provider(DummyAPIProvider.class).apiKey(this.apiKey)
					.apiSecret(this.apiSecret).build();
		}
	}

	public List<Collector> getCollectors(String role, String env, String type)
			throws BuildWSClientException {
		List<Collector> collectors = null;

		OAuthRequest request = new OAuthRequest(Verb.GET, this.endpoint + URI+"/"+env+"/"+role+"/"+type); 

		String jsonResponse = this.sendRequest(request);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		GetCollectorResponse response = gson.fromJson(jsonResponse,
				GetCollectorResponse.class);
		collectors = response.getCollectors();

		return collectors;
	}
	public List<Collector> postResult(CreateCollectorResultRequest r)
			throws BuildWSClientException {
		List<Collector> collectors = null;
		
		OAuthRequest request = new OAuthRequest(Verb.POST, this.endpoint + URI+"/result"); 
		request.addPayload(new Gson().toJson(r));
		System.out.println(request.getBodyContents());
		String jsonResponse = this.sendRequest(request);

		System.out.println(jsonResponse);

		return collectors;
	}


	public String sendRequest(OAuthRequest request)
			throws BuildWSClientException {
		request.addHeader("Content-Type", "application/json");

		Map<String, String> headers = request.getHeaders();

		if (DEBUG)
			logger.debug("Request Headers:");
		if (DEBUG)
			logger.debug("---------------------------------------");
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			if (DEBUG)
				logger.debug("    " + entry.getKey() + "=" + entry.getValue());
		}

		Token accessToken = new Token("", "");
		oAuthService.signRequest(accessToken, request);
		Response response = request.send();

		int code = response.getCode();
		logger.info("code=" + code);
		// String strMsg = response.getMessage();
		String jsonResponse = response.getBody();
		logger.info("jsonResponse=" + jsonResponse);

		// print response json pretty
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonResponse);
		String prettyJsonString = gson.toJson(je);
		if (DEBUG)
			logger.debug("\n" + prettyJsonString);

		if (code >= 300) {
			BuildWSClientException exception = new BuildWSClientException(
					jsonResponse);
			throw exception;
		}

		return jsonResponse;
	}

	private static String formatJSON(String jsonStr) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonStr);
		String prettyJsonString = gson.toJson(je);
		return prettyJsonString;
	}
}
