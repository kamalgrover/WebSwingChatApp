# 1. Use the official Java 17 image (Strictly required for Webswing 26.1)
FROM eclipse-temurin:17-jdk-jammy

# 2. Install required headless dependencies and Linux fonts (No xvfb-run trap)
RUN apt-get update && apt-get install --no-install-recommends -y \
    wget \
    unzip \
    xvfb \
    libxext6 \
    libxi6 \
    libxtst6 \
    libxrender1 \
    fontconfig \
    fonts-dejavu \
    && rm -rf /var/lib/apt/lists/*

# 3. Export DISPLAY explicitly so Java knows where the virtual monitor is
ENV WEBSWING_HOME=/opt/webswing \
    DISPLAY=:99

WORKDIR /opt/webswing

# 4. Copy and extract the Webswing binaries
COPY webswing.zip /tmp/webswing.zip
RUN unzip /tmp/webswing.zip -d /tmp/webswing_extracted && \
    mv /tmp/webswing_extracted/*/* /opt/webswing/ && \
    rm -rf /tmp/webswing.zip /tmp/webswing_extracted && \
    chmod +x /opt/webswing/webswing.sh

# 5. Copy your custom Java app and configuration
COPY build/libs/chat-app.jar /opt/webswing/apps/chat-app.jar
COPY webswing.config /opt/webswing/webswing.config

EXPOSE 8080

# 6. YOUR ORIGINAL FIX: Create a startup script that runs Xvfb in the background safely
RUN echo '#!/bin/sh' > start.sh && \
    echo 'Xvfb :99 -screen 0 1024x768x16 &' >> start.sh && \
    echo 'sleep 2' >> start.sh && \
    echo './webswing.sh run' >> start.sh && \
    chmod +x start.sh

# 7. Boot the server
CMD ["./start.sh"]