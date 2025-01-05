# See also 
# https://hub.docker.com/_/openjdk/tags?name=23-jdk-slim
# NOTE: 
#	- The Gradle Docker plugin does not support JDK 23 yet,
#	  so docker image generation, publishing, ... is done via shell scripts outside of gradle.
#   - https://plugins.gradle.org/plugin/com.bmuschko.docker-spring-boot-application
# 	- https://bmuschko.github.io/gradle-docker-plugin/current/user-guide/#spring_boot_application_plugin


FROM openjdk:23-jdk-slim


ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Expose port 8090
EXPOSE 8090

ENTRYPOINT ["java","-jar","/app.jar"]