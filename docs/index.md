---
title: UI for Apache Kafka
description: Fork-owned documentation for chenrui333/kafka-ui
hide:
  - navigation
  - toc
---

<div class="landing-page">
  <section class="hero-shell reveal is-visible">
    <div class="hero-grid">
      <div class="hero-copy">
        <p class="hero-kicker">Fork-owned documentation</p>
        <h1 class="hero-title">Operate Kafka with a sharper, production-first control surface.</h1>
        <p class="hero-lead">
          UI for Apache Kafka combines cluster visibility, topic tooling, auth controls, schema operations,
          and message inspection in one place. This fork keeps the documentation in the product repo, so
          runtime guidance, IAM support, release behavior, and build instructions move with the code instead
          of drifting in a separate docs system.
        </p>
        <div class="hero-actions">
          <a class="md-button md-button--primary" href="overview/getting-started/">Start with Docker</a>
          <a class="md-button" href="configuration/authentication/aws-iam/">EKS and MSK IAM</a>
          <a class="md-button" href="development/building/prerequisites/">Build from source</a>
        </div>
        <div class="hero-pills">
          <span class="hero-pill">Kafka 3.9.1 baseline</span>
          <span class="hero-pill">JDK 21</span>
          <span class="hero-pill">GHCR releases</span>
          <span class="hero-pill">EKS to MSK IAM on 9098</span>
        </div>
        <div class="hero-route-list">
          <a class="route-chip" href="quick-start/demo-run/">Demo route</a>
          <a class="route-chip" href="configuration/configuration-file/">Config route</a>
          <a class="route-chip" href="configuration/compose-examples/">Compose route</a>
          <a class="route-chip" href="project/roadmap/">Project route</a>
        </div>
      </div>
      <div class="hero-stack">
        <div class="hero-panel">
          <img src="assets/images/Interface.gif" alt="UI for Apache Kafka interface overview" />
          <p class="hero-caption">
            Cluster visibility, message browsing, consumer offsets, schema operations, and auth-heavy runtime
            configurations are documented next to the implementation they describe.
          </p>
        </div>
        <div class="hero-terminal">
          <div class="terminal-bar">
            <span class="terminal-dot"></span>
            <span class="terminal-dot"></span>
            <span class="terminal-dot"></span>
            <strong>fork runtime contract</strong>
          </div>
          <pre><code>docker run -it -p 8080:8080 \
  -e DYNAMIC_CONFIG_ENABLED=true \
  ghcr.io/chenrui333/kafka-ui:latest

# EKS + MSK IAM
security.protocol=SASL_SSL
sasl.mechanism=AWS_MSK_IAM
bootstrap.servers=&lt;broker&gt;:9098</code></pre>
          <div class="hero-meta-grid">
            <div class="hero-meta">
              <span>Image source</span>
              <strong>GitHub Container Registry</strong>
            </div>
            <div class="hero-meta">
              <span>Release model</span>
              <strong>Immutable GitHub releases</strong>
            </div>
            <div class="hero-meta">
              <span>Local toolchain</span>
              <strong>mise + JDK 21 + Node 24</strong>
            </div>
            <div class="hero-meta">
              <span>Primary target</span>
              <strong>Ops-heavy Kafka environments</strong>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <section class="home-section reveal">
    <p class="section-label">Choose your route</p>
    <h2 class="section-title">Three clean entry points, depending on the job you need to do</h2>
    <p class="section-copy">
      The documentation is organized for the workflows operators actually run: stand the UI up quickly,
      wire it into real infrastructure safely, or work on the fork itself without guessing where the project
      boundaries live.
    </p>
    <div class="lane-grid">
      <article class="lane-card">
        <span class="lane-tag">Operators</span>
        <h3>Run it now</h3>
        <p>Use the GHCR image, get a demo deployment up, then move into persistent config and Helm once you know the shape fits.</p>
        <div class="lane-links">
          <a href="quick-start/demo-run/">Demo run</a>
          <a href="quick-start/persistent-start/">Persistent start</a>
          <a href="configuration/configuration-file/">Configuration file</a>
        </div>
      </article>
      <article class="lane-card lane-card--accent">
        <span class="lane-tag">Platform</span>
        <h3>Secure the runtime</h3>
        <p>Auth, TLS, RBAC, data masking, IAM, and compose labs are grouped so the production-facing controls are easy to reach.</p>
        <div class="lane-links">
          <a href="configuration/authentication/aws-iam/">AWS IAM</a>
          <a href="configuration/rbac-role-based-access-control/">RBAC</a>
          <a href="configuration/ssl/">Kafka with SSL</a>
        </div>
      </article>
      <article class="lane-card">
        <span class="lane-tag">Contributors</span>
        <h3>Change the fork deliberately</h3>
        <p>Build instructions, contribution guidance, and the project roadmap stay in the same repo as the code and release flow.</p>
        <div class="lane-links">
          <a href="development/building/prerequisites/">Build prerequisites</a>
          <a href="development/contributing/">Contributing</a>
          <a href="project/roadmap/">Roadmap</a>
        </div>
      </article>
    </div>
  </section>

  <section class="home-section reveal band-shell">
    <div class="band-grid">
      <div class="band-copy">
        <p class="section-label section-label--light">Operational focus</p>
        <h2 class="section-title section-title--light">This fork’s runtime guidance is opinionated on purpose</h2>
        <p class="section-copy section-copy--light">
          The docs are not trying to be a generic upstream mirror. They call out the actual contract this fork
          is optimized around: GitHub-hosted releases, GHCR images, JDK 21 builds, Kafka 3.9.1 compatibility,
          and the EKS to MSK IAM shape on broker port `9098`.
        </p>
        <div class="band-pills">
          <span class="band-pill">Fork-owned docs</span>
          <span class="band-pill">Repo-linked compose labs</span>
          <span class="band-pill">Ambient AWS credential path</span>
          <span class="band-pill">No separate docs repo</span>
        </div>
      </div>
      <div class="command-card">
        <span class="command-label">application.yaml sketch</span>
        <pre><code>kafka:
  clusters:
    - name: msk-eks
      bootstrapServers: b-1.example.amazonaws.com:9098
      properties:
        security.protocol: SASL_SSL
        sasl.mechanism: AWS_MSK_IAM
        sasl.client.callback.handler.class: software.amazon.msk.auth.iam.IAMClientCallbackHandler
        sasl.jaas.config: software.amazon.msk.auth.iam.IAMLoginModule required;</code></pre>
      </div>
    </div>
  </section>

  <section class="home-section reveal">
    <p class="section-label">What this site owns now</p>
    <h2 class="section-title">Docs, examples, and release behavior now point at the same repo</h2>
    <p class="section-copy">
      The main value of this migration is operational integrity. Examples, screenshots, release instructions,
      and fork-specific behavior now live together, which removes the old split between product code and external docs.
    </p>
    <div class="signal-grid signal-grid--wide">
      <div class="signal-card">
        <strong>Runtime docs</strong>
        <h3>Configuration and auth stay local</h3>
        <p>Configuration wizard, file-based config, Helm guidance, RBAC, masking, TLS, and IAM all resolve inside this site.</p>
      </div>
      <div class="signal-card">
        <strong>Scenario examples</strong>
        <h3>Compose labs stay versioned with the code</h3>
        <p>Compose examples and their supporting SSL, JMX, JAAS, and proto assets are versioned alongside the application.</p>
      </div>
      <div class="signal-card">
        <strong>Release contract</strong>
        <h3>Images and releases match this fork</h3>
        <p>The docs point at `ghcr.io/chenrui333/kafka-ui` and the repository’s immutable release process, not a generic upstream image lane.</p>
      </div>
      <div class="signal-card">
        <strong>Contributor context</strong>
        <h3>Roadmap and build paths are explicit</h3>
        <p>Contributors can move from docs to build prerequisites to issues without having to discover a second docs repo or stale project board.</p>
      </div>
    </div>
    <div class="ops-strip">
      <div class="ops-stat">
        <span>Docs stack</span>
        <strong>MkDocs Material</strong>
      </div>
      <div class="ops-stat">
        <span>Pages source</span>
        <strong>GitHub Actions deploy</strong>
      </div>
      <div class="ops-stat">
        <span>Container source</span>
        <strong>GitHub Container Registry</strong>
      </div>
      <div class="ops-stat">
        <span>Best first stop</span>
        <strong>Getting started or AWS IAM</strong>
      </div>
    </div>
  </section>

  <section class="cta-banner reveal">
    <div>
      <p class="section-label">Start here</p>
      <h2 class="section-title">Pick the shortest path to a working deployment</h2>
      <p class="section-copy">
        If you are evaluating the fork, start with the GHCR demo image. If you are wiring it into a real cluster,
        jump straight to configuration or IAM. If you are extending the project, go to the build guide first.
      </p>
    </div>
    <div class="cta-actions">
      <a class="md-button md-button--primary" href="overview/getting-started/">Open getting started</a>
      <a class="md-button" href="configuration/compose-examples/">Browse compose examples</a>
    </div>
  </section>
</div>
