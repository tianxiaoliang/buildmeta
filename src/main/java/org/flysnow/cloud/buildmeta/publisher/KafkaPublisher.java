package org.flysnow.cloud.buildmeta.publisher;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.tool.Configuration;

import java.util.*;
import java.util.Map.Entry;

import kafka.common.QueueFullException;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.producer.async.*;

public class KafkaPublisher {
	private Logger log = Logger.getLogger(KafkaPublisher.class);
	private ProducerConfig config;
	private Producer<String, String> producer = null;
	final static int RECONNECTIONDELAY_DEFAULT = 20000;
	int reconnectionDelay = 1;
	Connector connector;
	int partitionSeqHitCnt = 0;
	int partitionKey = 0;
	int boundNum = 100000;
	final static int PARTITION_LEAP_CNT_INTERVAL_DEFAULT = 2000;
	final static int PARTITION_LEAP_TIME_INTERVAL_DEFAULT = 4000;
	int partitionLeapCntInterval = PARTITION_LEAP_CNT_INTERVAL_DEFAULT;
	int partitionLeapTimeInterval = PARTITION_LEAP_TIME_INTERVAL_DEFAULT;
	long partitionLeapStartTime = 0;
	public static int queueFullMsgCount = 0;
	boolean partitionKeyEnable = false;

	public void init() {
		Configuration.init();
		log.info("KafkaPublisher initializing");
		Properties p = Configuration.getProps();

		config = new ProducerConfig(p);
		producer = new Producer<String, String>(config);
		partitionKey = new Random().nextInt(boundNum);
		partitionLeapStartTime = System.currentTimeMillis();

		log.info("Kafka properties");
		Iterator<Entry<Object, Object>> propsItr = p.entrySet().iterator();
		while (propsItr.hasNext()) {
			Entry<Object, Object> prop = propsItr.next();
			log.info(prop.getKey() + " : " + prop.getValue());
		}
	}

	public void send(String topic, String msg) {
		KeyedMessage<String, String> data;
		data = new KeyedMessage<String, String>(topic, msg);

		try {
			producer.send(data);
		} catch (QueueFullException e) {
			if (queueFullMsgCount < Integer.MAX_VALUE - 1000) {
				queueFullMsgCount++;
			} else {
				log.warn("KAFKA QueueFullException Count: " + queueFullMsgCount);
			}
		} catch (IllegalQueueStateException e) {
			// TODO remove this catch clause if unused
			log.warn("KAFKA(IllegalQueueStateException): " + e.getClass()
					+ ": Kafka send error  = " + producer);
		} catch (NullPointerException e) {
			// TODO remove this catch clause if unused?
			// TODO handle unsentMsgList
			// unsentMsgList.add(msg);
			log.debug("KAFKA: producer=null, while try reconnect");
		} catch (RuntimeException e) {
			// unsentMsgList.add(msg);
			log.warn("KAFKA: " + e.getClass()
					+ ": Kafka send error, try reconnect: producer = "
					+ producer);
			if (reconnectionDelay > 0) {
				fireConnector();
			}
		}
	}

	void fireConnector() {
		synchronized (this) {
			if (this.connector == null) {
				this.connector = new Connector();
				log.info("kafka producer close");
				if (producer != null) {
					producer.close();
				}
				producer = null;

				log.info("Starting a new connector thread.");
				this.connector.setDaemon(true);
				this.connector.setPriority(1);
				this.connector.start();
			}
		}
	}

	synchronized public void close() {
		log.info("kafka producer close");
		if (producer != null) {
			producer.close();
		}
		producer = null;

		if (connector != null) {
			connector.interrupted = true;
			connector = null;
		}
	}

	class Connector extends Thread {
		boolean interrupted = false;

		public void run() {
			while (!interrupted) {
				try {
					try {
						Thread.sleep(reconnectionDelay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.debug("Attempting connection to Kafka");

					synchronized (this) {
						producer = new Producer<String, String>(config);
						connector = null;
						log.info("Kafka connection established. Exiting connector thread.");
						break;
					}
				} catch (RuntimeException e) {
					log.error("KAFKA: "
							+ e.getClass()
							+ ": Kafka connect error, try reconnect: producer = "
							+ producer);
					// Log.debug("Could not connect to " +
					// BridgeSocketAppender.this.address.getHostName() +
					// ". Exception is " + localIOException);
				}
			}
		}
	}
}