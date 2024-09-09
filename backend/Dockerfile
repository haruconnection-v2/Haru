#FROM openjdk:17
#
## 작업 디렉토리를 /src로 변경
#WORKDIR /src
#COPY --from=builder /backend/build/libs/*.jar app.jar
#
#
#ARG JAR_FILE
#RUN mkdir -p /var/log/spring-boot
#
#
## 애플리케이션 실행
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]