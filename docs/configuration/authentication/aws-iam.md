---
description: How to configure AWS IAM Authentication
---

# AWS IAM

UI for Apache Kafka comes with a built-in [aws-msk-iam-auth](https://github.com/aws/aws-msk-iam-auth) library.

Use the AWS MSK IAM SASL mechanism when Kafka UI needs to connect to MSK brokers over the IAM-enabled broker path.

For MSK, that usually means:

- `security.protocol: SASL_SSL`
- `sasl.mechanism: AWS_MSK_IAM`
- `sasl.client.callback.handler.class: software.amazon.msk.auth.iam.IAMClientCallbackHandler`
- broker endpoints on the IAM listener, typically port `9098`

More about permissions: [MSK and Serverless setup](../../quick-start/prerequisites/permissions/msk-+serverless-setup.md)

## EKS and IRSA note

This fork supports the common EKS deployment shape where Kafka UI runs in a pod and reaches MSK with IAM auth.

If Kafka UI runs with pod or service-account credentials, including IRSA, you can leave the AWS IAM fields blank in the configuration wizard. That path uses the default AWS credential provider chain and generates a JAAS stanza like:

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

If you want Kafka UI to assume a dedicated role before connecting, this fork also supports optional `awsRoleArn`, `awsRoleSessionName`, and `awsStsRegion` options in the generated JAAS config:

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

## Running from a container

```
docker run -p 8080:8080 \
    -e KAFKA_CLUSTERS_0_NAME=local \
    -e KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=<KAFKA_URL>:9098 \
    -e KAFKA_CLUSTERS_0_PROPERTIES_SECURITY_PROTOCOL=SASL_SSL \
    -e KAFKA_CLUSTERS_0_PROPERTIES_SASL_MECHANISM=AWS_MSK_IAM \
    -e KAFKA_CLUSTERS_0_PROPERTIES_SASL_CLIENT_CALLBACK_HANDLER_CLASS=software.amazon.msk.auth.iam.IAMClientCallbackHandler \
    -e KAFKA_CLUSTERS_0_PROPERTIES_SASL_JAAS_CONFIG=software.amazon.msk.auth.iam.IAMLoginModule required awsProfileName="<PROFILE_NAME>"; \
    -d ghcr.io/chenrui333/kafka-ui:latest
```

Replace:

- `<KAFKA_URL>` with your MSK broker list
- `<PROFILE_NAME>` with an AWS profile name when you are using shared credentials instead of ambient pod credentials

## Configuring with `application.yaml`

```yaml
kafka:
  clusters:
    - name: local
      bootstrapServers: <KAFKA_URL>:9098
      properties:
        security.protocol: SASL_SSL
        sasl.mechanism: AWS_MSK_IAM
        sasl.client.callback.handler.class: software.amazon.msk.auth.iam.IAMClientCallbackHandler
        sasl.jaas.config: software.amazon.msk.auth.iam.IAMLoginModule required awsProfileName="<PROFILE_NAME>";
```

## Kafka Connect caveat

If Kafka Connect workers also need IAM auth to talk to MSK, treat that as a separate runtime concern from Kafka UI itself.

The upstream `aws-msk-iam-auth` guidance is to place the IAM auth jar on the Kafka Connect worker classpath outside the plugin path. This fork does not build or own Kafka Connect runtime images.
