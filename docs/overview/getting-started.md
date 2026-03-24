# Getting started

To run UI for Apache Kafka, you can use either a pre-built Docker image or build it (or a jar file) yourself.

### Quick start (Demo run)

```
docker run -it -p 8080:8080 -e DYNAMIC_CONFIG_ENABLED=true ghcr.io/chenrui333/kafka-ui:latest
```

Then access the web UI at [http://localhost:8080](http://localhost:8080)

The command is sufficient to try things out. When you're done trying things out, you can proceed with a [persistent installation](../quick-start/persistent-start.md).

### Persistent installation

```
services:
  kafka-ui:
    container_name: kafka-ui
    image: ghcr.io/chenrui333/kafka-ui:latest
    ports:
      - 8080:8080
    environment:
      DYNAMIC_CONFIG_ENABLED: true
    volumes:
      - ~/kui/config.yml:/etc/kafkaui/dynamic_config.yaml
```

Please refer to the [configuration file guide](../configuration/configuration-file.md) to proceed with further app configuration.

### Some useful configuration-related links

- [Web UI Cluster Configuration Wizard](../configuration/configuration-wizard.md)
- [Configuration file explanation](../configuration/configuration-file.md)
- [Docker Compose examples](../configuration/compose-examples.md)
- [Misc configuration properties](../configuration/misc-configuration-properties.md)

### Helm charts

- [Quick start](../configuration/helm-charts/quick-start.md)

### Building from sources

- [Prerequisites](../development/building/prerequisites.md)

### Liveliness and readiness probes

Liveliness and readiness endpoint is at `/actuator/health`.\
Info endpoint (build info) is located at `/actuator/info`.

## Configuration options

All of the environment variables and config properties are documented in [Misc configuration properties](../configuration/misc-configuration-properties.md).

## Contributing

Please refer to the [contributing guide](../development/contributing.md); it is the source of truth for this fork.
