# WebSwing Chat App
Enterprise Java Swing Application in the Browser

![Java](https://img.shields.io/badge/Java-17-orange)
![Webswing](https://img.shields.io/badge/Webswing-26.1-blue)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
![Gradle](https://img.shields.io/badge/Gradle-Build-green)

## Overview
A high-performance Java Swing chat application deployed as a modern web service using **Webswing 26.1**. This project demonstrates a containerized architecture running a "headless" Java GUI on a virtual X11 framebuffer (Xvfb) inside a Linux Docker container.

## Architecture
- **Frontend:** Webswing HTML5/WebSocket bridge.
- **Backend:** Java 17 (Eclipse Temurin) running Spring Framework.
- **GUI:** Swing with FlatLaf modern Look & Feel.
- **Deployment:** Multi-stage Docker setup with automated Xvfb display management.

## Prerequisites
- Docker and Docker Compose
- Java 17 JDK
- Webswing 26.1 Distribution (`webswing.zip` in project root)

## Setup & Installation

### 1. Extract API Libraries
To satisfy Gradle dependencies without an external Nexus repository (Air-gapped fix):
```bash
mkdir libs
unzip -j webswing.zip \"*webswing-api*.jar\" -d libs/
```

### 2. Build the Application
Generate the shadow JAR containing all dependencies:
```bash
./gradlew shadowJar
```

### 3. Launch with Docker
Build the headless environment and start the server:
```bash
docker-compose build --no-cache
docker-compose up
```

### Usage
Once the container is running, access the application at:
```bash
http://localhost:8080/chat
```
> [!NOTE]
> If you see an "Evaluation Version" banner, this is normal for the community edition and does not restrict functionality for educational purposes.

### Troubleshooting
Once the container is running, access the application at:
- Blank Screen: Check `webswing.config` to ensure the `mainClass` is correctly defined within the `launcherConfig` block (Webswing 26.1+ schema requirement).
- Connection Reset: Verify the Docker port mapping (`8080:8080`) and ensure `webswing.server.host` is set to `0.0.0.0` in `webswing.properties`.
- Logs: Real-time logs are mapped to the local `/logs` directory on your host machine for easy debugging.

### License
Educational/Personal Use - Webswing Community Edition.
