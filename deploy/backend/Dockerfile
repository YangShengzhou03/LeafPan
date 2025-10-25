FROM openjdk:17-jre-alpine
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV TZ=Asia/Shanghai
WORKDIR /app
COPY backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]