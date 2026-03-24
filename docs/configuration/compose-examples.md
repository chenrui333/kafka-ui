---
description: Curated Docker Compose scenarios for local Kafka UI labs
---

# Compose examples

The compose labs for this fork live in [`documentation/compose/`](https://github.com/chenrui333/kafka-ui/tree/main/documentation/compose)
and are versioned in the same repo as the application code.

Use the catalog below to pick a scenario quickly.

| Scenario | File | Notes |
| --- | --- | --- |
| Default local sandbox | [`kafka-ui.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ui.yaml) | General-purpose lab with Kafka UI, brokers, Schema Registry, Kafka Connect, and demo topics. |
| ARM64 local sandbox | [`kafka-ui-arm64.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ui-arm64.yaml) | ARM-friendly setup for Apple Silicon or other ARM64 hosts. |
| SASL auth | [`kafka-ui-sasl.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ui-sasl.yaml) | Kafka SASL authentication path. |
| Basic auth with custom context | [`kafka-ui-auth-context.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ui-auth-context.yaml) | Username/password auth plus a custom URL path context. |
| TLS and SSL | [`kafka-ssl.yml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ssl.yml) | Kafka TLS and SSL connectivity examples. |
| Schema Registry auth | [`kafka-cluster-sr-auth.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-cluster-sr-auth.yaml) | Schema Registry with authentication enabled. |
| JMX over SSL | [`kafka-ui-jmx-secured.yml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ui-jmx-secured.yml) | JMX with SSL and auth. |
| JMX exporter | [`kafka-ui-with-jmx-exporter.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ui-with-jmx-exporter.yaml) | Prometheus-oriented JMX exporter path. |
| Kafka Connect auth | [`kafka-ui-connectors-auth.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ui-connectors-auth.yaml) | Connector auth examples. |
| SerDe examples | [`kafka-ui-serdes.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ui-serdes.yaml) | Ready-to-run serialization and custom SerDe setups. |
| ACL with ZooKeeper | [`kafka-ui-acl-with-zk.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-ui-acl-with-zk.yaml) | ACL path for ZooKeeper-backed setups. |
| Nginx reverse proxy | [`nginx-proxy.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/nginx-proxy.yaml) | Reverse proxy example with nginx. |
| Traefik reverse proxy | [`traefik-proxy.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/traefik-proxy.yaml) | Reverse proxy example with Traefik. |
| Kafka with ZooKeeper | [`kafka-with-zookeeper.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/kafka-with-zookeeper.yaml) | Legacy broker mode with ZooKeeper. |
| E2E lab | [`e2e-tests.yaml`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/e2e-tests.yaml) | Test-oriented lab with connectors and KSQL. |

## Supporting assets

Some compose files rely on adjacent helper directories in the same repo:

- [`documentation/compose/ssl/`](https://github.com/chenrui333/kafka-ui/tree/main/documentation/compose/ssl)
- [`documentation/compose/jaas/`](https://github.com/chenrui333/kafka-ui/tree/main/documentation/compose/jaas)
- [`documentation/compose/jmx/`](https://github.com/chenrui333/kafka-ui/tree/main/documentation/compose/jmx)
- [`documentation/compose/proto/`](https://github.com/chenrui333/kafka-ui/tree/main/documentation/compose/proto)
- [`documentation/compose/scripts/`](https://github.com/chenrui333/kafka-ui/tree/main/documentation/compose/scripts)

## Reference index

For the short descriptions of every compose file in one place, see
[`documentation/compose/DOCKER_COMPOSE.md`](https://github.com/chenrui333/kafka-ui/blob/main/documentation/compose/DOCKER_COMPOSE.md).
