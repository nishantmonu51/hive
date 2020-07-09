package org.apache.hive.kafka;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.service.AbstractService;
import org.apache.hive.druid.MiniDruidCluster;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.google.common.base.Throwables;
import com.google.common.io.Files;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * This class has the hooks to start and stop single node kafka cluster.
 *
 */
public class SingleNodeKafkaCluster extends AbstractService {
  private static final Logger log = LoggerFactory.getLogger(SingleNodeKafkaCluster.class);
  private static final int BROKER_PORT = 9092;
  private static final String LOCALHOST = "localhost";


  private final KafkaServerStartable serverStartable;
  private final int brokerPort;
  private final String kafkaServer;

  public SingleNodeKafkaCluster(String name, String logDir, Integer zkPort, Integer brokerPort){
    super(name);
    Properties properties = new Properties();
    this.brokerPort = brokerPort == null ? BROKER_PORT : brokerPort;
    File dir = new File(logDir);
    if (dir.exists()) {
      // need to clean data directory to ensure that there is no interference from old runs
      // Cleaning is happening here to allow debugging in case of tests fail
      // we don;t have to clean logs since it is an append mode
      log.info("Cleaning the druid directory [{}]", dir.getAbsolutePath());
      try {
        FileUtils.deleteDirectory(dir);
      } catch (IOException e) {
        log.error("Failed to clean druid directory");
        throw new RuntimeException(e);
      }
    }
    String zkString = String.format("localhost:%d", zkPort);

    this.kafkaServer = String.format("%s:%d", LOCALHOST, this.brokerPort);

    properties.setProperty("zookeeper.connect", zkString);
    properties.setProperty("broker.id", String.valueOf(1));
    properties.setProperty("host.name", LOCALHOST);
    properties.setProperty("port", Integer.toString(brokerPort));
    properties.setProperty("log.dir", logDir);
    // This property is very important, we are sending form records with a specific time
    // Thus need to make sure that they don't get DELETED
    properties.setProperty("log.retention.hours", String.valueOf(Integer.MAX_VALUE));
    properties.setProperty("log.flush.interval.messages", String.valueOf(1));
    properties.setProperty("offsets.topic.replication.factor", String.valueOf(1));
    properties.setProperty("offsets.topic.num.partitions", String.valueOf(1));
    properties.setProperty("transaction.state.log.replication.factor", String.valueOf(1));
    properties.setProperty("transaction.state.log.min.isr", String.valueOf(1));
    properties.setProperty("log.cleaner.dedupe.buffer.size", "1048577");

    this.serverStartable = new KafkaServerStartable(KafkaConfig.fromProps(properties));
  }


  @Override
  protected void serviceStart() throws Exception {
    serverStartable.startup();
    log.info("Kafka Server Started on port {}", brokerPort);

  }

  @Override
  protected void serviceStop() throws Exception {
    log.info("Stopping Kafka Server");
    serverStartable.shutdown();
    log.info("Kafka Server Stopped");
  }

  /**
   * Creates a topic and inserts data from the specified datafile.
   * Each line in the datafile is sent to kafka as a single message.
   * @param topicName
   * @param datafile
   */
  public void createTopicWithData(String topicName, File datafile){
    createTopic(topicName);
    // set up kafka producer
    Properties properties = new Properties();
    properties.put("bootstrap.servers", kafkaServer);
    properties.put("acks", "1");
    properties.put("retries", "3");

    try(KafkaProducer<String, String> producer = new KafkaProducer<>(
        properties,
        new StringSerializer(),
        new StringSerializer()
    )){
      List<String> events = Files.readLines(datafile, Charset.forName("UTF-8"));
      for(String event : events){
        producer.send(new ProducerRecord<>(topicName, "key", event));
      }
    } catch (IOException e) {
      Throwables.propagate(e);
    }
  }

  public void createTopicWithData(String topic, List<byte []> events) {
    createTopic(topic);
    // set up kafka producer
    Properties properties = new Properties();
    properties.put("bootstrap.servers", kafkaServer);
    properties.put("acks", "1");
    properties.put("retries", "3");

    try(KafkaProducer<byte[], byte[]> producer = new KafkaProducer<>(
        properties,
        new ByteArraySerializer(),
        new ByteArraySerializer()
    )){
      // 1534736225090 -> 08/19/2018 20:37:05
      IntStream.range(0, events.size())
          .mapToObj(i -> new ProducerRecord<>(topic,
              0,
              // 1534736225090 -> Mon Aug 20 2018 03:37:05
              1534736225090L + 1000 * 3600 * i,
              ("key-" + i).getBytes(),
              events.get(i)))
          .forEach(r -> producer.send(r));
    }
  }

  private void createTopic(String topic) {
    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092");
    properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

    int numPartitions = 1;
    short replicationFactor = 1;
    AdminClient adminClient = AdminClient.create(properties);
    NewTopic newTopic = new NewTopic(topic, numPartitions, replicationFactor);

    adminClient.createTopics(Collections.singletonList(newTopic));
    adminClient.close();
  }

}
