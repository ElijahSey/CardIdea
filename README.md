# cardIDEA
[![Continuous Integration Workflow](https://github.com/ElijahSey/cardIDEA/actions/workflows/integration.yml/badge.svg)](https://github.com/ElijahSey/cardIDEA/actions/workflows/integration.yml)
[![Continuous Deployment Workflow](https://github.com/ElijahSey/cardIDEA/actions/workflows/deployment.yml/badge.svg)](https://github.com/ElijahSey/cardIDEA/actions/workflows/deployment.yml)

## Continuous Integration

### Trigger

The workflow is triggered by every push to the main branch.

### Execution

The application is built by maven and all tests are run.

## Continuous Deployment

### Trigger

The workflow is triggered manually.
It needs the version number as input.

### Execution

- The application is built.
- Installers are created for Windows, macOS and Linux.
- A new release is created using the content of the CHANGELOG.md file as text
- The installers are attached to the release
