# 🚀 LinkedIn Microservices Architecture

An enterprise-grade, event-driven microservices ecosystem that replicates the core networking, content sharing, and notification features of LinkedIn. This project demonstrates distributed system design, asynchronous event streaming, polyglot persistence, and cloud-native architectural patterns.

## 🏗️ Architecture & Services

The system routes all client traffic through a centralized API Gateway, resolving instances via a Eureka Discovery Server. The microservices are fully decoupled, communicating asynchronously via Apache Kafka to prevent data loss and ensure eventual consistency. 

### Core Microservices
* **API Gateway:** Centralized entry point handling request routing, load balancing, and security boundaries.
* **Discovery Server:** Spring Cloud Eureka server for dynamic service registration and discovery.
* **User Service:** Manages secure JWT-based authentication, user registration, and profile data. Backed by **PostgreSQL**.
* **Connections Service:** Manages the social graph, handling connection requests (send, accept, reject) and calculating multi-degree network algorithms. Backed by **Neo4j**.
* **Post Service:** Handles the creation, retrieval, and interaction (likes/unlikes) of user-generated feed content. Backed by **PostgreSQL**.
* **Uploader Service:** Dedicated service for managing file and media uploads associated with user posts.
* **Notification Service:** Consumes Kafka events asynchronously to generate real-time alerts for user interactions (e.g., new connection requests, post likes). Backed by **PostgreSQL**.

## 🛠️ Tech Stack

| Category | Technologies |
| :--- | :--- |
| **Backend** | Java, Spring Boot, Spring Cloud (Gateway, Eureka), Spring Security |
| **Messaging** | Apache Kafka (KRaft mode) |
| **Databases** | PostgreSQL, Neo4j, Spring Data JPA, Spring Data Neo4j |
| **Infrastructure** | Docker, Docker Compose, Kubernetes |
| **Security** | JSON Web Tokens (JWT), BCrypt Hashing |

## ✨ Key Technical Features

* **Polyglot Persistence:** Strategically pairs relational databases (PostgreSQL) for transactional entity data with a graph database (Neo4j) for highly connected, relationship-heavy social graph data.
* **Event-Driven Data Synchronization:** Utilizes Kafka topics to asynchronously broadcast domain events (user creation, connection requests, post interactions) across service boundaries, eliminating tight coupling and synchronous HTTP bottlenecks.
* **Graph-Based Network Algorithms:** Leverages Neo4j Cypher queries to efficiently traverse millions of user nodes, calculating 1st and 2nd-degree connections in milliseconds.
* **Service Discovery & Routing:** Implements Spring Cloud Eureka and API Gateway for dynamic scaling, allowing instances to spin up and register without hardcoded IP routing.
* **Resilient Infrastructure:** Fully containerized environment using Docker Compose with built-in health checks, custom bridge networking, and persistent volume mounts for all databases.

## 🚀 Getting Started

### Prerequisites
* Java 21+
* Docker & Docker Compose
* Maven

### Local Setup (Docker Compose)

1. Clone the repository:
   ```bash
   git clone [https://github.com/md-zuhairi/LinkedIn_Microservices_Clone.git](https://github.com/md-zuhairi/LinkedIn_Microservices_Clone.git)
   cd LinkedIn_Microservices_Clone
   ```
2. Build the microservices (Run this inside each service directory):
   ```bash
   ./mvnw clean package -DskipTests
   ```
3. Spin up the infrastructure:
   ```bash
   docker-compose up -d
   ```
Note: This spins up Neo4j, 3x PostgreSQL instances, Kafka, and the required network bridges. Wait for the containers to report a healthy status.

4. Start the Spring Boot applications via your IDE or Docker. Start Order:

* Discovery Server

* API Gateway

* Microservices (User, Connections, Post, Uploader, Notification)

5. Start the individual Spring Boot applications.

  **Kubernetes Deployment**
  Apply the manifests to spin up the cluster network, databases, and application pods:
  ```bash
  kubectl apply -f k8s/
  ```
  Verify the pods are running correctly in the namespace:
  ```bash
  kubectl get pods -n shuttle-apps
  ```

## 📡 API Reference

All requests should be routed through the API Gateway (Default Port: `8080`).

### Authentication & Users
* **POST** `/api/v1/users/auth/signup` - Register a new user (`name`, `email`, `password`)
* **POST** `/api/v1/users/auth/login` - Authenticate and receive JWT

### Social Graph (Connections)
* **POST** `/api/v1/connections/core/request/{userId}` - Send a connection request
* **POST** `/api/v1/connections/core/accept/{userId}` - Accept a connection request
* **POST** `/api/v1/connections/core/reject/{userId}` - Reject a connection request
* **GET** `/api/v1/connections/core/{userId}/first-degree` - Fetch 1st-degree network
* **GET** `/api/v1/connections/core/{userId}/second-degree` - Fetch 2nd-degree network

### Content Feed (Posts)
* **POST** `/api/v1/posts/core` - Create a new post (Accepts `Text` and `File`)
* **GET** `/api/v1/posts/core/{postId}` - Retrieve a specific post
* **GET** `/api/v1/posts/core/users/{userId}/allPosts` - Retrieve all posts by a specific user
* **POST** `/api/v1/posts/likes/{postId}` - Like a post
* **DELETE** `/api/v1/posts/likes/{postId}` - Unlike a post

---
*Designed and engineered by Mohamed Zuhairi.*
