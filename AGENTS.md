# Repo Notes

## Local Development

This repository does not maintain a `.devcontainer` or Codespaces workflow.

Use local Docker or Docker Compose for runtime dependencies and use the host-managed toolchain for source builds. For the repo-managed frontend toolchain, use `mise` and follow [kafka-ui-react-app/README.md](kafka-ui-react-app/README.md).

## GitHub API Usage

For GitHub API-backed maintenance in this repository, prefer authenticated `gh` commands and `gh auth token` over unauthenticated API access.

If a tool supports `GITHUB_TOKEN`, wire it from `gh auth token` so version lookups and workflow maintenance do not fail on the anonymous rate limit.
