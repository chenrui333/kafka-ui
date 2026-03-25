#!/usr/bin/env bash

set -euo pipefail

tag="${1:?usage: write_release_notes.sh <tag>}"

if ! git rev-parse -q --verify "refs/tags/${tag}" >/dev/null; then
  echo "tag not found: ${tag}" >&2
  exit 1
fi

mapfile -t tags < <(git tag --sort=-v:refname)

previous_tag=""
for candidate in "${tags[@]}"; do
  if [[ "${candidate}" != "${tag}" ]]; then
    previous_tag="${candidate}"
    break
  fi
done

range="${tag}"
if [[ -n "${previous_tag}" ]]; then
  range="${previous_tag}..${tag}"
fi

repo_slug="${GITHUB_REPOSITORY:-}"
if [[ -z "${repo_slug}" ]]; then
  remote_url="$(git remote get-url origin 2>/dev/null || true)"
  remote_url="${remote_url#git@github.com:}"
  remote_url="${remote_url#https://github.com/}"
  remote_url="${remote_url%.git}"
  repo_slug="${remote_url}"
fi

echo "## Changes"
echo

if [[ -n "${previous_tag}" ]]; then
  echo "_Changes since \`${previous_tag}\`._"
  echo
fi

mapfile -t changes < <(git log --no-merges --format='%s' "${range}")

if [[ "${#changes[@]}" -eq 0 ]]; then
  echo "- No changes"
else
  printf -- '- %s\n' "${changes[@]}"
fi

echo
echo "## Docker"
echo
echo '```'
echo "docker pull ghcr.io/${repo_slug}:${tag}"
echo '```'
echo
echo "Multi-arch image (linux/amd64, linux/arm64)."

if [[ -n "${previous_tag}" && -n "${repo_slug}" ]]; then
  echo
  echo "## Full Changelog"
  echo
  echo "- https://github.com/${repo_slug}/compare/${previous_tag}...${tag}"
fi
