![UI for Apache Kafka logo](documentation/images/kafka-ui-logo.png) UI for Apache Kafka&nbsp;
------------------
#### Versatile, fast and lightweight web UI for managing Apache Kafka® clusters. Built by developers, for developers.
<br/>

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
![UI for Apache Kafka Price Free](documentation/images/free-open-source.svg)
[![Release version](https://img.shields.io/github/v/release/chenrui333/kafka-ui)](https://github.com/chenrui333/kafka-ui/releases)

<p align="center">
    <a href="https://github.com/chenrui333/kafka-ui">REPOSITORY</a> •
    <a href="https://github.com/chenrui333/kafka-ui/issues">ISSUES</a> •
    <a href="https://github.com/chenrui333/kafka-ui/pulls">PULL REQUESTS</a> •
    <a href="https://chenrui333.github.io/kafka-ui/">DOCS</a>
</p>

<p align="center">
  <img src="https://repobeats.axiom.co/api/embed/2e8a7c2d711af9daddd34f9791143e7554c35d0f.svg" />
</p>

#### UI for Apache Kafka is a free, open-source web UI to monitor and manage Apache Kafka clusters.

UI for Apache Kafka is a simple tool that makes your data flows observable, helps find and troubleshoot issues faster and deliver optimal performance. Its lightweight dashboard makes it easy to track key metrics of your Kafka clusters - Brokers, Topics, Partitions, Production, and Consumption.

> Maintenance note: this fork is planned for archival. For ongoing development and support, use the actively maintained successor project at [kafbat/kafka-ui](https://github.com/kafbat/kafka-ui).

> Fork note: `chenrui333/kafka-ui` was maintained as a fork of the upstream Provectus project. Existing fork-owned documentation remains at `https://chenrui333.github.io/kafka-ui/`, and historical fork-specific issues and pull requests stay in this repository.

> Local development note: this fork does not maintain a `.devcontainer` or Codespaces workflow. Use local Docker or Docker Compose for services and the host-managed toolchain for source builds. For the repo-managed frontend toolchain, see [kafka-ui-react-app/README.md](kafka-ui-react-app/README.md).

Set up UI for Apache Kafka with just a couple of easy commands to visualize your Kafka data in a comprehensible way. You can run the tool locally or in
the cloud.

![Interface](documentation/images/Interface.gif)

# Features
* **Multi-Cluster Management** — monitor and manage all your clusters in one place
* **Performance Monitoring with Metrics Dashboard** —  track key Kafka metrics with a lightweight dashboard
* **View Kafka Brokers** — view topic and partition assignments, controller status
* **View Kafka Topics** — view partition count, replication status, and custom configuration
* **View Consumer Groups** — view per-partition parked offsets, combined and per-partition lag
* **Browse Messages** — browse messages with JSON, plain text, and Avro encoding
* **Dynamic Topic Configuration** — create and configure new topics with dynamic configuration
* **Configurable Authentication** — [secure](https://chenrui333.github.io/kafka-ui/configuration/authentication) your installation with optional Github/Gitlab/Google OAuth 2.0
* **Custom serialization/deserialization plugins** - [use](https://chenrui333.github.io/kafka-ui/configuration/serialization-serde) a ready-to-go serde for your data like AWS Glue or Smile, or code your own!
* **Role based access control** - [manage permissions](https://chenrui333.github.io/kafka-ui/configuration/rbac-role-based-access-control) to access the UI with granular precision
* **Data masking** - [obfuscate](https://chenrui333.github.io/kafka-ui/configuration/data-masking) sensitive data in topic messages

# The Interface
UI for Apache Kafka wraps major functions of Apache Kafka with an intuitive user interface.

![Interface](documentation/images/Interface.gif)

## Topics
UI for Apache Kafka makes it easy for you to create topics in your browser by several clicks,
pasting your own parameters, and viewing topics in the list.

![Create Topic](documentation/images/Create_topic_kafka-ui.gif)

It's possible to jump from connectors view to corresponding topics and from a topic to consumers (back and forth) for more convenient navigation.
connectors, overview topic settings.

![Connector_Topic_Consumer](documentation/images/Connector_Topic_Consumer.gif)

### Messages
Let's say we want to produce messages for our topic. With the UI for Apache Kafka we can send or write data/messages to the Kafka topics without effort by specifying parameters, and viewing messages in the list.

![Produce Message](documentation/images/Create_message_kafka-ui.gif)

## Schema registry
There are 3 supported types of schemas: Avro®, JSON Schema, and Protobuf schemas.

![Create Schema Registry](documentation/images/Create_schema.gif)

Before producing avro/protobuf encoded messages, you have to add a schema for the topic in Schema Registry. Now all these steps are easy to do
with a few clicks in a user-friendly interface.

![Avro Schema Topic](documentation/images/Schema_Topic.gif)

# Getting Started

To run UI for Apache Kafka, you can use either a pre-built Docker image or build it (or a jar file) yourself.

## Quick start (GHCR demo image)

```
docker run -it -p 8080:8080 -e DYNAMIC_CONFIG_ENABLED=true ghcr.io/chenrui333/kafka-ui:latest
```

Then access the web UI at [http://localhost:8080](http://localhost:8080)

When you're done trying things out, you can proceed with a [persistent installation](https://chenrui333.github.io/kafka-ui/quick-start/persistent-start)

## Persistent installation

```
services:
  kafka-ui:
    container_name: kafka-ui
    image: ghcr.io/chenrui333/kafka-ui:latest
    ports:
      - 8080:8080
    environment:
      DYNAMIC_CONFIG_ENABLED: 'true'
    volumes:
      - ~/kui/config.yml:/etc/kafkaui/dynamic_config.yaml
```

Please refer to the [configuration file guide](https://chenrui333.github.io/kafka-ui/configuration/configuration-file) to proceed with further app configuration.

## Some useful configuration related links

[Web UI Cluster Configuration Wizard](https://chenrui333.github.io/kafka-ui/configuration/configuration-wizard)

[Configuration file explanation](https://chenrui333.github.io/kafka-ui/configuration/configuration-file)

[Docker Compose examples](https://chenrui333.github.io/kafka-ui/configuration/compose-examples)

[Misc configuration properties](https://chenrui333.github.io/kafka-ui/configuration/misc-configuration-properties)

## Helm charts

[Quick start](https://chenrui333.github.io/kafka-ui/configuration/helm-charts/quick-start)

## Building from sources

[Quick start](https://chenrui333.github.io/kafka-ui/development/building/prerequisites) with building

## Liveliness and readiness probes
Liveliness and readiness endpoint is at `/actuator/health`.<br/>
Info endpoint (build info) is located at `/actuator/info`.

# Configuration options

All of the environment variables/config properties could be found [here](https://chenrui333.github.io/kafka-ui/configuration/misc-configuration-properties).

## EKS to MSK IAM on 9098

This fork supports the common EKS deployment shape where `kafka-ui` connects to an MSK cluster with IAM auth on broker port `9098`.

For runtime auth, use:

- `security.protocol: SASL_SSL`
- `sasl.mechanism: AWS_MSK_IAM`
- `sasl.client.callback.handler.class: software.amazon.msk.auth.iam.IAMClientCallbackHandler`

If `kafka-ui` runs with pod or service-account credentials in EKS, including IRSA, you can leave the AWS IAM fields blank in the cluster configuration wizard. That path uses the default AWS credential provider chain and generates:

```yaml
kafka:
  clusters:
    - name: msk-eks
      bootstrapServers: b-1.example.kafka.us-east-1.amazonaws.com:9098
      properties:
        security.protocol: SASL_SSL
        sasl.mechanism: AWS_MSK_IAM
        sasl.client.callback.handler.class: software.amazon.msk.auth.iam.IAMClientCallbackHandler
        sasl.jaas.config: software.amazon.msk.auth.iam.IAMLoginModule required;
```

If you want `kafka-ui` to assume a dedicated IAM role before connecting to MSK, the cluster wizard also supports optional `awsRoleArn`, `awsRoleSessionName`, and `awsStsRegion` fields. That maps to JAAS config like:

```yaml
kafka:
  clusters:
    - name: msk-eks
      bootstrapServers: b-1.example.kafka.us-east-1.amazonaws.com:9098
      properties:
        security.protocol: SASL_SSL
        sasl.mechanism: AWS_MSK_IAM
        sasl.client.callback.handler.class: software.amazon.msk.auth.iam.IAMClientCallbackHandler
        sasl.jaas.config: >-
          software.amazon.msk.auth.iam.IAMLoginModule required
          awsRoleArn="arn:aws:iam::123456789012:role/msk-ui"
          awsRoleSessionName="kafka-ui"
          awsStsRegion="us-east-1";
```

If Kafka Connect workers also need IAM auth to talk to MSK, treat that as a separate runtime concern from `kafka-ui` itself. The upstream `aws-msk-iam-auth` guidance is to place that jar on the Kafka Connect worker classpath outside the plugin path; this fork does not build or own Kafka Connect runtime images.

# Contributing

Please refer to [CONTRIBUTING.md](CONTRIBUTING.md), which is the source of truth for contribution workflow in this fork.
