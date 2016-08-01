FROM maven:3-jdk-8

ADD . $MAVEN_HOME

RUN cd $MAVEN_HOME \
 && mvn -B clean package -Pdocker-build \
 && mv $MAVEN_HOME/target /signing \
 && apt update \
 && apt install -y ruby \
 && gem install --no-ri --no-rdoc asciidoctor-pdf --pre \
 && rm -r /var/lib/apt/lists \
 && rm -r $MAVEN_HOME

ONBUILD ADD . /signing/docs

ONBUILD VOLUME /signing/conf

ONBUILD EXPOSE 8080
ONBUILD EXPOSE 8081

ONBUILD ENTRYPOINT ["sh", "/signing/bin/run.sh"]
