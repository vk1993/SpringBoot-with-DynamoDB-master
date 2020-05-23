FROM openjdk:8-alpine
MAINTAINER VISAL!!!
EXPOSE 8183
WORKDIR /app
VOLUME /temp
ADD ./target/dynamoDB-*.jar /app/app.jar
CMD ["java","-jar","/app/app.jar"]