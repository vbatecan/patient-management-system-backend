FROM openjdk:21

COPY . /app

WORKDIR /app

RUN chmod +x gradlew && \
    ./gradlew clean build -x test && \
    cp build/libs/*.jar app.jar