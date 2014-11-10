package org.flysnow.cloud.buildmeta.publisher;

import java.io.*;
import java.util.UUID;

import org.elasticsearch.client.Client;  
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.flysnow.cloud.buildmeta.client.ESConnectionManager;
import org.flysnow.cloud.buildmeta.domain.model.CollectorResult;
import org.flysnow.cloud.buildmeta.tool.Configuration; 
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ElasticSearchPublisher {
	static class ElasticSearchPublisherHolder {
		public static ElasticSearchPublisher publisher = new ElasticSearchPublisher();
	}

	public static ElasticSearchPublisher getResource() {
		return ElasticSearchPublisherHolder.publisher;
	}

	private ElasticSearchPublisher() {
		connectElasticsearch();
	}

	private static Logger log = Logger.getLogger(ElasticSearchPublisher.class);
	private int timeout = 10000;
	private Client client = null;

	// Elasticsearch indexing
	private volatile BulkRequestBuilder currentRequest;
	private int bulkSize = 50;

	private String elasticsearch_searchCluster;
	private String elasticsearch_farmName;

	// connect to elasticsearch server
	public void connectElasticsearch() {
		Configuration.init();
		bulkSize = Integer.parseInt(Configuration.getValue("es.bulksize"));
		ESConnectionManager manager = new ESConnectionManager();

		client = manager.getClient(Configuration.getValue("es.servers"),
				"es.cluster.name");
		if (client == null) {
			log.info("esconsumer fail to connect elasticsearch cluster ");
			System.exit(1);
		}
		log.info("elasticsearches are connected");
		currentRequest = client.prepareBulk();

	}

	public String getIndexName(String domain, String envId) {
		String index = domain.replace("\\.", "-") + ":" + envId;
		return index;
	}

	public void send(CollectorResult result) {
		try {
			String indexName = "";
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			// parse the json data and get the indexName

			long indexlong = (result.getTime() / (86400000));
			UUID uid = UUID.randomUUID();
			result.setUid(uid.toString());
			// save the json data in the buffer.
			if (indexName != null) {
				indexName = getIndexName(result.getDomain(), result.getEnv());
				messageToBuffer(indexName, result.getUid(),
						new Gson().toJson(result));

				if (currentRequest.numberOfActions() > bulkSize)
					sendBulkRequestBulksize(bulkSize);
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("file Writing error");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("bulkRequest error");
			log.error("error: " + e.getMessage());
		}
	}

	synchronized public void messageToBuffer(String indexName, String id,
			String json) throws Exception {
		log.info("add to request");
		currentRequest.add(client.prepareIndex(indexName,
				elasticsearch_farmName, id).setSource(json));
	}

	// Bulk buffer is more than bulksize and send the bulk data to
	// elasticsearch.
	// In that case need to cancel before timer and set new timer again.
	synchronized public void sendBulkRequestBulksize(int bulksize) {
		log.info("Bulksize working");
		BulkResponse response = currentRequest.execute().actionGet();
		if (response.hasFailures()) {
			log.info("failed to execute  : " + response.buildFailureMessage());
		} else {
			log.info("index " + bulksize + " messages");
		}

		currentRequest = client.prepareBulk();
	}

}
