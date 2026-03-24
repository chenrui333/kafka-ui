package com.provectus.kafka.ui.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.DescribeAclsResult;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.acl.AclBinding;
import org.apache.kafka.common.acl.AclBindingFilter;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ReactiveAdminClientCompatibilityTest {

  @Test
  void detectKafkaVersionPrefersMetadataVersionOverInterBrokerProtocolVersion() {
    String version = ReactiveAdminClient.detectKafkaVersion(List.of(
        new ConfigEntry("inter.broker.protocol.version", "3.8-IV0"),
        new ConfigEntry("metadata.version", "3.9-IV0")
    ));

    assertThat(version).isEqualTo("3.9-IV0");
  }

  @Test
  void detectKafkaVersionFallsBackToInterBrokerProtocolVersionForLegacyConfigs() {
    String version = ReactiveAdminClient.detectKafkaVersion(List.of(
        new ConfigEntry("inter.broker.protocol.version", "3.5")
    ));

    assertThat(version).isEqualTo("3.5");
  }

  @Test
  void detectKafkaVersionReturnsUnknownWhenBrokerConfigsDoNotExposeVersion() {
    String version = ReactiveAdminClient.detectKafkaVersion(List.of(
        new ConfigEntry("delete.topic.enable", "false")
    ));

    assertThat(version).isEqualTo(ReactiveAdminClient.UNKNOWN_VERSION);
  }

  @Test
  void topicDeletionFlagDefaultsToTrueWhenConfigIsMissing() {
    assertThat(ReactiveAdminClient.extractTopicDeletionEnabled(List.of())).isTrue();
  }

  @Test
  void supportedFeaturesUseMetadataDerivedVersionForModernKafka() {
    AdminClient adminClient = mock(AdminClient.class);
    DescribeAclsResult describeAclsResult = mock(DescribeAclsResult.class);
    when(adminClient.describeAcls(AclBindingFilter.ANY)).thenReturn(describeAclsResult);
    when(describeAclsResult.values()).thenReturn(KafkaFuture.completedFuture(List.<AclBinding>of()));

    StepVerifier.create(ReactiveAdminClient.SupportedFeature.forVersion(adminClient, "3.9-IV0"))
        .assertNext(features -> assertThat(features)
            .contains(
                ReactiveAdminClient.SupportedFeature.INCREMENTAL_ALTER_CONFIGS,
                ReactiveAdminClient.SupportedFeature.CONFIG_DOCUMENTATION_RETRIEVAL,
                ReactiveAdminClient.SupportedFeature.DESCRIBE_CLUSTER_INCLUDE_AUTHORIZED_OPERATIONS,
                ReactiveAdminClient.SupportedFeature.AUTHORIZED_SECURITY_ENABLED
            ))
        .verifyComplete();
  }
}
