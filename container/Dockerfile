# ベースとなるDockerイメージを指定
FROM maven:3.8.5-openjdk-17-slim AS builder

# アプリケーションのソースコードをコンテナにコピー
COPY . /app
WORKDIR /app

# Mavenを使用してアプリケーションをビルド
RUN mvn clean package -DskipTests=true

# 実行ステージ
FROM openjdk:17-slim

# ビルドステージから jar をコピー
COPY --from=builder /app/target/hello-world-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

# アプリケーションを実行する
CMD ["java", "-jar", "app.jar"]