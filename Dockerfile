# --- build stage (Gradle) ---
FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /app

# copy file cấu hình trước để cache dependency
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x gradlew

# tải dependency trước cho nhanh (không bắt buộc)
RUN ./gradlew --no-daemon dependencies || true

# copy source
COPY src ./src

# build jar (skip tests)
RUN ./gradlew --no-daemon bootJar -x test

# --- runtime stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# copy file jar đã build (đường dẫn Gradle mặc định)
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]