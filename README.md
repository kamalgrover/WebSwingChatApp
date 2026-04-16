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
- Webswing 26.1 Distribution (`webswing.zip` in project root)

## Setup & Installation

### Option A: Hardened preflight flow (clear messages)
This option validates required files and prints actionable error messages before Docker runs.

macOS/Linux:
```bash
./scripts/dev-up.sh
```

Windows PowerShell:
```powershell
.\scripts\dev-up.ps1
```

What it checks:
- `webswing.zip` exists in project root
- then runs `docker-compose up --build`

### Option B: Direct compose flow (default, minimal setup)
`docker-compose.yml` now uses the multi-stage Dockerfile by default. The JAR is built inside Docker, so host Java/Gradle setup is not required.

```bash
docker-compose up --build
```

Required on all OSes:
- Docker Desktop (or Docker Engine + Compose)
- `webswing.zip` in project root

Wrapper files that should stay in git:
- `gradlew`
- `gradlew.bat`
- `gradle/wrapper/gradle-wrapper.jar`
- `gradle/wrapper/gradle-wrapper.properties`

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
- Build fails with `COPY webswing.zip ... not found`: place `webswing.zip` in the project root.
- Build fails while extracting Webswing API jars: verify your `webswing.zip` is a valid Webswing 26.1 distribution archive.

### License
Educational/Personal Use - Webswing Community Edition.
