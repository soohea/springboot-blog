FROM java:openjdk:17.0-jdk

RUN mkdir /app

WORKDIR /app

COPY target/springboot-blog-1.0-SNAPSHOT.jar /app

EXPOSE 9000

CMD ["java","-jar","springboot-blog-1.0-SNAPSHOT.jar"]