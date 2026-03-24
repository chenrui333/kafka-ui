package com.provectus.kafka.ui.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.provectus.kafka.ui.AbstractIntegrationTest;
import com.provectus.kafka.ui.model.ConsumerPosition;
import com.provectus.kafka.ui.model.KafkaCluster;
import com.provectus.kafka.ui.model.SeekDirectionDTO;
import com.provectus.kafka.ui.model.SeekTypeDTO;
import com.provectus.kafka.ui.model.TopicMessageDTO;
import com.provectus.kafka.ui.model.TopicMessageEventDTO;
import com.provectus.kafka.ui.producer.KafkaTestProducer;
import com.provectus.kafka.ui.serdes.builtin.StringSerde;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class KafkaCompatibilitySmokeTest extends AbstractIntegrationTest {

  @Autowired
  MessagesService messagesService;

  private KafkaCluster cluster;
  private ReactiveAdminClient reactiveAdminClient;

  @BeforeEach
  void init() {
    cluster = applicationContext
        .getBean(ClustersStorage.class)
        .getClusterByName(LOCAL)
        .orElseThrow();
    reactiveAdminClient = applicationContext
        .getBean(AdminClientService.class)
        .get(cluster)
        .block();
  }

  @Test
  void smokePathCoversAdminAndBrowsingOperationsOnCurrentKafkaBaseline() throws Exception {
    String topic = "compat-smoke-" + UUID.randomUUID();
    TopicPartition tp = new TopicPartition(topic, 0);
    String groupId = "compat-smoke-group-" + UUID.randomUUID();

    try {
      createTopic(new NewTopic(topic, 1, (short) 1));

      StepVerifier.create(reactiveAdminClient.describeCluster())
          .assertNext(description -> {
            assertThat(description.getClusterId()).isNotBlank();
            assertThat(description.getNodes()).isNotEmpty();
          })
          .verifyComplete();

      StepVerifier.create(reactiveAdminClient.updateTopicConfig(
              topic,
              Map.of(
                  "cleanup.policy", "delete",
                  "retention.ms", "60000"
              )))
          .verifyComplete();

      try (AdminClient admin = createAdminClient()) {
        Config config = admin.describeConfigs(
            List.of(new ConfigResource(ConfigResource.Type.TOPIC, topic)))
            .all()
            .get()
            .get(new ConfigResource(ConfigResource.Type.TOPIC, topic));
        assertThat(config.get("cleanup.policy").value()).isEqualTo("delete");
        assertThat(config.get("retention.ms").value()).isEqualTo("60000");
      }

      try (var producer = KafkaTestProducer.forKafka(kafka)) {
        producer.send(topic, "message-1").get();
        producer.send(topic, "message-2").get();
      }

      try (KafkaConsumer<String, String> consumer = createConsumer(groupId)) {
        consumer.subscribe(List.of(topic));
        int polled = 0;
        while (polled < 2) {
          polled += consumer.poll(Duration.ofMillis(100)).count();
        }
        consumer.commitSync(Map.of(tp, new OffsetAndMetadata(2)));
      }

      StepVerifier.create(reactiveAdminClient.listConsumerGroupOffsets(List.of(groupId), List.of(tp)))
          .assertNext(offsets -> assertThat(offsets.row(groupId)).containsEntry(tp, 2L))
          .verifyComplete();

      Flux<TopicMessageDTO> messages = messagesService.loadMessages(
              cluster,
              topic,
              new ConsumerPosition(SeekTypeDTO.BEGINNING, topic, null),
              null,
              null,
              10,
              SeekDirectionDTO.FORWARD,
              StringSerde.name(),
              StringSerde.name())
          .filter(event -> event.getType() == TopicMessageEventDTO.TypeEnum.MESSAGE)
          .map(TopicMessageEventDTO::getMessage)
          .take(2);

      StepVerifier.create(messages.map(TopicMessageDTO::getContent))
          .expectNext("message-1", "message-2")
          .verifyComplete();
    } finally {
      deleteTopic(topic);
    }
  }

  private static AdminClient createAdminClient() {
    Properties properties = new Properties();
    properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
    return AdminClient.create(properties);
  }

  private static KafkaConsumer<String, String> createConsumer(String groupId) {
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    return new KafkaConsumer<>(properties);
  }
}
