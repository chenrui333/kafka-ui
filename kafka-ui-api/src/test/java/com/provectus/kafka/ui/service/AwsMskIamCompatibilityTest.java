package com.provectus.kafka.ui.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.provectus.kafka.ui.config.ClustersProperties;
import com.provectus.kafka.ui.emitter.PollingSettings;
import com.provectus.kafka.ui.model.KafkaCluster;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import javax.security.auth.login.AppConfigurationEntry;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.types.Password;
import org.apache.kafka.common.security.JaasContext;
import org.junit.jupiter.api.Test;

class AwsMskIamCompatibilityTest {

  private static final String BOOTSTRAP_SERVERS = "localhost:9098";
  private static final String IAM_LOGIN_MODULE = "software.amazon.msk.auth.iam.IAMLoginModule";
  private static final String IAM_CALLBACK_HANDLER =
      "software.amazon.msk.auth.iam.IAMClientCallbackHandler";

  @Test
  void awsMskIamLibraryClassesAreAvailableOnClasspath() throws Exception {
    assertThat(Class.forName(IAM_LOGIN_MODULE)).isNotNull();
    assertThat(Class.forName(IAM_CALLBACK_HANDLER)).isNotNull();
  }

  @Test
  void kafkaParsesDocumentedAwsMskIamJaasOptions() {
    AppConfigurationEntry entry = loadJaasEntry(
        IAM_LOGIN_MODULE + " required "
            + "awsRoleArn=\"arn:aws:iam::123456789012:role/msk_client_role\" "
            + "awsRoleSessionName=\"msk-static-session\" "
            + "awsStsRegion=\"us-east-1\";"
    );

    assertThat(entry.getLoginModuleName()).isEqualTo(IAM_LOGIN_MODULE);
    assertThat(entry.getOptions().get("awsRoleArn"))
        .isEqualTo("arn:aws:iam::123456789012:role/msk_client_role");
    assertThat(entry.getOptions().get("awsRoleSessionName")).isEqualTo("msk-static-session");
    assertThat(entry.getOptions().get("awsStsRegion")).isEqualTo("us-east-1");
  }

  @Test
  void kafkaClientConstructorsAcceptAwsMskIamConfig() {
    Properties clusterProperties = awsMskIamProperties(
        IAM_LOGIN_MODULE + " required "
            + "awsRoleArn=\"arn:aws:iam::123456789012:role/msk_client_role\" "
            + "awsRoleSessionName=\"msk-static-session\" "
            + "awsStsRegion=\"us-east-1\";"
    );
    KafkaCluster cluster = kafkaCluster(clusterProperties);

    assertThatCode(() -> {
      AdminClient adminClient = AdminClient.create(adminClientProperties(clusterProperties));
      adminClient.close(Duration.ZERO);
      var producer = MessagesService.createProducer(cluster, Map.of());
      producer.close(Duration.ZERO);
      var consumer = new ConsumerGroupService(null, null).createConsumer(
          cluster,
          Map.of(ConsumerConfig.GROUP_ID_CONFIG, "aws-msk-iam-compat")
      );
      consumer.close(Duration.ZERO);
    }).doesNotThrowAnyException();
  }

  private static AppConfigurationEntry loadJaasEntry(String jaasConfig) {
    JaasContext jaasContext = JaasContext.loadClientContext(Map.of(
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL",
        SaslConfigs.SASL_MECHANISM, "AWS_MSK_IAM",
        SaslConfigs.SASL_JAAS_CONFIG, new Password(jaasConfig)
    ));
    assertThat(jaasContext.configurationEntries()).hasSize(1);
    return jaasContext.configurationEntries().get(0);
  }

  private static Properties adminClientProperties(Properties clusterProperties) {
    Properties properties = new Properties();
    properties.putAll(clusterProperties);
    properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 1_000);
    properties.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 1_000);
    properties.put(AdminClientConfig.CLIENT_ID_CONFIG, "aws-msk-iam-admin-compat");
    return properties;
  }

  private static Properties awsMskIamProperties(String jaasConfig) {
    Properties properties = new Properties();
    properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
    properties.put(SaslConfigs.SASL_MECHANISM, "AWS_MSK_IAM");
    properties.put(SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS, IAM_CALLBACK_HANDLER);
    properties.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
    return properties;
  }

  private static KafkaCluster kafkaCluster(Properties clusterProperties) {
    return KafkaCluster.builder()
        .originalProperties(new ClustersProperties.Cluster())
        .name("aws-msk-iam-compat")
        .bootstrapServers(BOOTSTRAP_SERVERS)
        .properties(clusterProperties)
        .pollingSettings(PollingSettings.createDefault())
        .build();
  }
}
