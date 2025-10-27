# --- build stage (Gradle) ---
FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /app

# copy file cấu hình trước để cache dependency
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x gradlew

# tải dependency (tùy chọn, để cache cho lần sau)
RUN ./gradlew --no-daemon dependencies || true

# copy source code
COPY src ./src

# build jar (bỏ test để nhanh hơn)
RUN ./gradlew --no-daemon bootJar -x test

# --- runtime stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# copy jar từ stage build
COPY --from=build /app/build/libs/*.jar app.jar


# expose port
EXPOSE 8080

# run app
ENTRYPOINT ["java","-jar","/app/app.jar"]
