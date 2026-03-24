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
      <div>
        <p class="hero-kicker">Fork-owned documentation</p>
        <h1 class="hero-title">Operate Kafka with a cleaner control surface.</h1>
        <p class="hero-lead">
          UI for Apache Kafka combines cluster visibility, topic tooling, auth controls, and message inspection
          in one place. This fork keeps the docs in the product repo, so deployment guidance, IAM support, and
          build instructions now move with the code instead of drifting in a separate docs system.
        </p>
        <div class="hero-actions">
          <a class="md-button md-button--primary" href="overview/getting-started/">Start with Docker</a>
          <a class="md-button" href="configuration/authentication/aws-iam/">EKS and MSK IAM</a>
          <a class="md-button" href="configuration/compose-examples/">Compose lab files</a>
        </div>
        <div class="hero-pills">
          <span class="hero-pill">Kafka 3.9.1 baseline</span>
          <span class="hero-pill">JDK 21</span>
          <span class="hero-pill">GHCR releases</span>
          <span class="hero-pill">EKS to MSK IAM on 9098</span>
        </div>
      </div>
      <div class="hero-panel">
        <img src="assets/images/Interface.gif" alt="UI for Apache Kafka interface overview" />
        <p class="hero-caption">
          Cluster visibility, message browsing, consumer offsets, schema operations, and auth-heavy runtime
          configurations are documented side by side with the implementation they describe.
        </p>
      </div>
    </div>
  </section>

  <section class="home-section reveal">
    <p class="section-label">Jump directly to the work</p>
    <h2 class="section-title">Built for fast starts and operational depth</h2>
    <p class="section-copy">
      The docs are organized around the workflows operators actually need: get the app running quickly, wire it
      into auth and Kafka infrastructure safely, and keep development guidance close to the codebase.
    </p>
    <div class="card-grid">
      <a class="value-card" href="quick-start/demo-run/">
        <span class="card-tag">Quick start</span>
        <h3>Launch a working UI in minutes</h3>
        <p>Use the fork-owned GHCR image for a fast demo run, then move to a persistent install when you are ready.</p>
      </a>
      <a class="value-card" href="configuration/authentication/aws-iam/">
        <span class="card-tag">AWS IAM</span>
        <h3>Cover the EKS to MSK path cleanly</h3>
        <p>Ambient credentials, assumed-role flows, and the `9098` broker path are documented for this fork’s target shape.</p>
      </a>
      <a class="value-card" href="configuration/compose-examples/">
        <span class="card-tag">Compose lab</span>
        <h3>Find the right sandbox fast</h3>
        <p>Compose examples are curated by scenario so you can grab the exact lab file you need without hunting through the repo.</p>
      </a>
      <a class="value-card" href="development/building/prerequisites/">
        <span class="card-tag">Build from source</span>
        <h3>Work on the fork directly</h3>
        <p>Local build instructions, contributor workflow, and repo-managed toolchain expectations live in one place.</p>
      </a>
    </div>
  </section>

  <section class="home-section reveal">
    <p class="section-label">Operational focus</p>
    <h2 class="section-title">What this docs site owns now</h2>
    <p class="section-copy">
      The goal is straightforward: the fork owns its own docs, its own runtime guidance, and its own design language.
      You should not need a separate upstream docs repo to understand how this fork is meant to be run or extended.
    </p>
    <div class="signal-grid">
      <div class="signal-card">
        <strong>Runtime docs</strong>
        <h3>Configuration and auth live here</h3>
        <p>Configuration wizard, file-based config, Helm guidance, RBAC, masking, TLS, and IAM all resolve inside this site.</p>
      </div>
      <div class="signal-card">
        <strong>Repo-linked examples</strong>
        <h3>Examples stay versioned with the code</h3>
        <p>Compose labs and supporting assets stay in the repo, so docs and runnable examples change together.</p>
      </div>
      <div class="signal-card">
        <strong>Fork identity</strong>
        <h3>Release and build paths match this fork</h3>
        <p>Container references point to `ghcr.io/chenrui333/kafka-ui`, and contributor guidance points back to this repository.</p>
      </div>
    </div>
    <div class="ops-strip">
      <div class="ops-stat">
        <span>Docs stack</span>
        <strong>MkDocs Material</strong>
      </div>
      <div class="ops-stat">
        <span>Release lane</span>
        <strong>Immutable GitHub releases</strong>
      </div>
      <div class="ops-stat">
        <span>Container source</span>
        <strong>GitHub Container Registry</strong>
      </div>
      <div class="ops-stat">
        <span>Local build baseline</span>
        <strong>mise + JDK 21 + Node 24</strong>
      </div>
    </div>
  </section>
</div>
