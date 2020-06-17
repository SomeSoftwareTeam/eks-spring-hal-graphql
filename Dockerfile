# BUILD STAGE
FROM gradle:6.3-jdk14 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble

# RUN STAGE
FROM openjdk:14-slim
RUN apt-get update && apt-get install -y \
  nano \
  watch \
  sudo

VOLUME /tmp
COPY --from=builder /home/gradle/src/build/libs/graphql-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]