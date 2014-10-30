package org.flysnow.cloud.buildmeta.client;

import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ESConnectionManager {
	private static Logger log = Logger.getLogger(ESConnectionManager.class);

	public Client getClient(String servers,String clusterName) {
		String[] array = servers.split(",");
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", clusterName)
				.put("client.transport.sniff", true).build();
		TransportClient client = new TransportClient(settings);

		for (String server : array) {
			String[] s = server.split(":");
			client.addTransportAddress(new InetSocketTransportAddress(s[0],
					Integer.parseInt(s[1])));
		}
		if (client != null)
			log.info("elasticsearches are connected");

		return client;
	}

	public void release(Client client) {
		client.close();
	}
}
