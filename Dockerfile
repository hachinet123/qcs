FROM maven:3.6.3-openjdk-11-slim  AS builder

# add pom.xml and source code
ADD ./settings.xml /tmp/ck-web-back/settings.xml
ADD ./pom.xml /tmp/ck-web-back/pom.xml
ADD ./src /tmp/ck-web-back/src/

# package jar
RUN cd /tmp/ck-web-back && mvn clean package --settings ./settings.xml -Dmaven.test.skip=true

# Second stage: minimal runtime environment
FROM openjdk:11.0.16

RUN ln -snf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime && echo Asia/Tokyo > /etc/timezone
RUN sed -i 's/TLSv1, TLSv1.1,//' /usr/local/openjdk-11/conf/security/java.security
RUN sed -i 's/3DES_EDE_CBC,//' /usr/local/openjdk-11/conf/security/java.security

# copy jar from the first stage
COPY --from=builder /tmp/ck-web-back/target/centralkitchen.jar /root/centralkitchen.jar

CMD [ "java", "-jar", "/root/centralkitchen.jar" ]

EXPOSE 8083
