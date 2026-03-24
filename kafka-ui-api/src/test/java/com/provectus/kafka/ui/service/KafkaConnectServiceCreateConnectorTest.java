package com.provectus.kafka.ui.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.provectus.kafka.ui.client.RetryingKafkaConnectClient;
import com.provectus.kafka.ui.connect.model.Connector;
import com.provectus.kafka.ui.mapper.ClusterMapper;
import com.provectus.kafka.ui.mapper.KafkaConnectMapperImpl;
import com.provectus.kafka.ui.model.ConnectorStateDTO;
import com.provectus.kafka.ui.model.ConnectorTypeDTO;
import com.provectus.kafka.ui.model.KafkaCluster;
import com.provectus.kafka.ui.model.NewConnectorDTO;
import com.provectus.kafka.ui.util.ReactiveFailover;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class KafkaConnectServiceCreateConnectorTest {

  @Test
  void createConnectorFallsBackToCreatedPayloadWhenConnectorReadIsEventuallyConsistent() {
    var client = mock(RetryingKafkaConnectClient.class);
    when(client.getConnectors(null)).thenReturn(Flux.just("[]"));

    var connectorName = "test-connector";
    var createdConnector = new Connector()
        .name(connectorName)
        .type(Connector.TypeEnum.SINK)
        .config(Map.of(
            "connector.class", "org.apache.kafka.connect.file.FileStreamSinkConnector",
            "tasks.max", "1",
            "topics", "output-topic",
            "file", "/tmp/test",
            "test.password", "test-credentials"
        ));
    when(client.createConnector(any())).thenReturn(Mono.just(createdConnector));

    var startupReadError = WebClientResponseException.create(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        HttpHeaders.EMPTY,
        new byte[0],
        StandardCharsets.UTF_8
    );
    when(client.getConnector(connectorName)).thenReturn(Mono.error(startupReadError));

    var service = new KafkaConnectService(
        mock(ClusterMapper.class),
        new KafkaConnectMapperImpl(),
        new ObjectMapper(),
        new KafkaConfigSanitizer(true, List.of())
    );

    var cluster = KafkaCluster.builder()
        .name("local")
        .connectsClients(Map.of("kafka-connect", ReactiveFailover.createNoop(client)))
        .build();

    var connectorRequest = Mono.just(new NewConnectorDTO()
        .name(connectorName)
        .config(createdConnector.getConfig()));

    StepVerifier.create(service.createConnector(cluster, "kafka-connect", connectorRequest))
        .assertNext(connector -> {
          assertThat(connector.getName()).isEqualTo(connectorName);
          assertThat(connector.getConnect()).isEqualTo("kafka-connect");
          assertThat(connector.getType()).isEqualTo(ConnectorTypeDTO.SINK);
          assertThat(connector.getStatus().getState()).isEqualTo(ConnectorStateDTO.UNASSIGNED);
          assertThat(connector.getConfig())
              .containsEntry("connector.class", "org.apache.kafka.connect.file.FileStreamSinkConnector")
              .containsEntry("test.password", "******");
        })
        .verifyComplete();
  }
}
