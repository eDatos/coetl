FROM centos:centos7

MAINTAINER Apereo Foundation

ENV PATH=$PATH:$JRE_HOME/bin

RUN yum -y install wget tar unzip git \
    && yum -y clean all


# Download Azul Java, verify the hash, and install \
RUN set -x; \
    java_version=8.0.112; \
    zulu_version=8.19.0.1; \
    java_hash=3f95d82bf8ece272497ae2d3c5b56c3b; \
    
    cd / \
    && wget http://cdn.azul.com/zulu/bin/zulu$zulu_version-jdk$java_version-linux_x64.tar.gz \
    && echo "$java_hash  zulu$zulu_version-jdk$java_version-linux_x64.tar.gz" | md5sum -c - \
    && tar -zxvf zulu$zulu_version-jdk$java_version-linux_x64.tar.gz -C /opt \
    && rm zulu$zulu_version-jdk$java_version-linux_x64.tar.gz \
    && ln -s /opt/zulu$zulu_version-jdk$java_version-linux_x64/jre/ /opt/jre-home;

RUN cd / \
	&& wget http://cdn.azul.com/zcek/bin/ZuluJCEPolicies.zip \
    && unzip ZuluJCEPolicies.zip \
    && mv -f ZuluJCEPolicies/*.jar /opt/jre-home/lib/security \
    && rm ZuluJCEPolicies.zip; 


# Download the CAS overlay project \
RUN git clone --depth 1 --single-branch https://github.com/apereo/cas-overlay-template.git cas-overlay \
    && mkdir -p /etc/cas \
    && mkdir -p /etc/cas/services \
    && mkdir -p /etc/cas/config \
    && mkdir -p cas-overlay/bin \;
    
RUN chmod -R 750 /cas-overlay/bin \
    && chmod 750 /cas-overlay/mvnw \
    && chmod 750 /cas-overlay/build.sh \
    && chmod 750 /opt/jre-home/bin/java;
    
COPY etc/cas/ /cas-overlay/etc/cas/
COPY bin/*.* /cas-overlay/bin/
COPY extradependencies.pom.xml /cas-overlay/

# ADD EXTRA POM DEPENDENCIES
RUN sed -i -e '/<\/dependency>/r /cas-overlay/extradependencies.pom.xml' /cas-overlay/pom.xml    

EXPOSE 8080 8443

VOLUME /etc/cas

WORKDIR /cas-overlay

ENV JAVA_HOME /opt/jre-home
ENV PATH $PATH:$JAVA_HOME/bin:.

ARG DEV=false
RUN /cas-overlay/bin/overwrite-dev-properties.sh

RUN ./mvnw clean package -T 10

CMD ["/cas-overlay/bin/run-cas.sh"]