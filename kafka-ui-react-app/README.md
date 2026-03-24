# UI for Apache Kafka
UI for Apache Kafka management

## Table of contents
- [Requirements](#requirements)
- [Getting started](#getting-started)
- [Links](#links)

## Requirements
- [docker](https://www.docker.com/get-started) (required to run [Initialize application](#initialize-application))
- [mise](https://mise.jdx.dev/) with the repo-managed Node.js and pnpm versions

Optional fallback if you do not use mise:
- [Node.js](https://nodejs.org/en/) 24.x
- [Corepack](https://nodejs.org/api/corepack.html) enabled for the pinned pnpm version

## Getting started

Install the repo-managed toolchain from the repository root
```sh
mise install
```

Go to react app folder
```sh
cd ./kafka-ui-react-app
```

If you are not using mise, enable the pinned pnpm version with Corepack
```
npm install --global corepack@latest
corepack enable pnpm
corepack install
```

Install dependencies
```
pnpm install
```

Generate API clients from OpenAPI document
```sh
pnpm gen:sources
```

## Start application
### Proxying API Requests in Development

Create or update existing `.env.local` file with
```
VITE_DEV_PROXY= https://api.server # your API server
```

Run the application
```sh
pnpm dev
```

### Docker way

Have to be run from root directory.

Start UI for Apache Kafka with your Kafka clusters:
```sh
docker-compose -f ./documentation/compose/kafka-ui.yaml up
```

Make sure that none of the `.env*` files contain `DEV_PROXY` variable

Run the application
```sh
pnpm dev
```
## Links

* [Vite](https://github.com/vitejs/vite)
