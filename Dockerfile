FROM maven:3-jdk-8

ADD . $MAVEN_HOME

RUN cd $MAVEN_HOME \
 && mvn -B clean package -Pdocker-build \
 && mv $MAVEN_HOME/target /signing \
 && apt update \
 && apt install -y ruby \
 && gem install asciidoctor-pdf --pre \
 && rm -r /var/lib/apt/lists \
 && rm -r $MAVEN_HOME

VOLUME /signing/docs

EXPOSE 8080
EXPOSE 8081

ENTRYPOINT ["sh", "/signing/bin/run.sh"]
