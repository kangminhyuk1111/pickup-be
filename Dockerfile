# 멀티 스테이지 빌드를 사용한 Dockerfile
# Stage 1: 빌드 스테이지
FROM eclipse-temurin:17-jdk-alpine AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 또는 Maven 파일 복사 (의존성 캐싱을 위해)
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

# 또는 Maven을 사용하는 경우:
# COPY pom.xml mvnw ./
# COPY .mvn .mvn

# 의존성 다운로드 (캐시 최적화)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드 (Lombok 어노테이션 처리 포함)
RUN ./gradlew clean bootJar --no-daemon

# Stage 2: 실행 스테이지
FROM eclipse-temurin:17-jre-alpine

# 애플리케이션 실행을 위한 사용자 생성 (보안 강화)
RUN addgroup -g 1001 spring && adduser -u 1001 -G spring -s /bin/sh -D spring
USER spring:spring

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]

# JVM 옵션 추가 (선택사항)
# ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]