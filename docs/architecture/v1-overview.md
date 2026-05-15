# Bastion V1 Architecture Overview

## Goal

Bastion V1 is a lightweight distributed node management system where desktop agents connect to a central Spring Boot master server.

The system allows:

* node registration
* heartbeat monitoring
* remote command execution
* command result collection
* basic observability through structured logging

The purpose of V1 is to learn:

* backend architecture
* networking
* concurrency
* distributed system fundamentals
* observability
* protocol design

---

# High-Level Architecture

The system contains three major components:

## 1. Master Server (Spring Boot)

Responsibilities:

* Accept TCP connections from nodes
* Maintain node registry
* Track node heartbeats
* Dispatch commands to nodes
* Expose REST APIs
* Persist structured event logs

Tech Stack:

* Java 21
* Spring Boot
* Raw TCP sockets
* Scheduled tasks

---

## 2. Node Agent

Responsibilities:

* Connect to master server
* Register itself
* Send periodic heartbeats
* Execute shell commands
* Return command output

Tech Stack:

* Java
* TCP sockets

---

## 3. Write-Ahead Log (WAL)

Responsibilities:

* Persist important system events
* Improve debuggability
* Enable future observability integrations

Examples:

* node registration
* command execution
* heartbeat failures
* disconnect events

---

# Communication Model

Communication happens over a custom TCP-based application protocol.

Message Format:

TYPE|NODE_ID|PAYLOAD

Examples:

REGISTER|node-01|{"os":"linux"}

HEARTBEAT|node-01|{"cpu":15}

COMMAND_RESULT|node-01|{"output":"hello"}

---

# V1 Constraints

This version intentionally avoids:

* microservices
* distributed queues
* cloud deployment
* advanced security
* Kubernetes
* complex orchestration

The goal is building a stable, understandable core system first.

---

# Future Extensions

Potential future improvements:

* Android node agents
* Prometheus/Grafana monitoring
* AWS deployment
* distributed job queues
* secure node authentication
* protocol V2

These are intentionally excluded from V1 scope.
