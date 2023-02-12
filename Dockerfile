# FROM quay.io/wildfly/wildfly
FROM jboss/wildfly:24.0.0.Final

ENV JBOSS_HOME /opt/jboss/wildfly

ARG DATASOURCE_DB_URL
ARG DATASOURCE_DB_USERNAME
ARG DATASOURCE_DB_PASSWORD 

ENV DATASOURCE_DB_URL ${DATASOURCE_DB_URL}
ENV DATASOURCE_DB_USERNAME ${DATASOURCE_DB_USERNAME}
ENV DATASOURCE_DB_PASSWORD ${DATASOURCE_DB_PASSWORD}

USER root

#Tell Wildfly to use our configuration over theirs
COPY module.xml $JBOSS_HOME/modules/system/layers/base/org/postgresql/main/
COPY standalone.xml $JBOSS_HOME/standalone/configuration/

COPY log4j2.xml $JBOSS_HOME/standalone/configuration/
COPY postgresql-42.5.0.jar $JBOSS_HOME/modules/system/layers/base/org/postgresql/main/

# ADD ./bin/adapter-elytron-install-offline.cli $JBOSS_HOME/bin
# ADD keycloak-oidc-wildfly-adapter-20.0.3.zip $JBOSS_HOME
# ADD ./bin/ $JBOSS_HOME/bin/
# ADD ./modules/ $JBOSS_HOME/modules/

# RUN unzip $JBOSS_HOME/keycloak-oidc-wildfly-adapter-20.0.3.zip
# RUN $JBOSS_HOME/bin/jboss-cli.sh --file=$JBOSS_HOME/bin/adapter-elytron-install-offline.cli

RUN /bin/sh -c '$JBOSS_HOME/bin/standalone.sh &' && \
sleep 10 && \
$JBOSS_HOME/bin/jboss-cli.sh --connect --command="deploy $JBOSS_HOME/modules/system/layers/base/org/postgresql/main/postgresql-42.5.0.jar" && \
$JBOSS_HOME/bin/jboss-cli.sh --connect --command="/system-property=log4j.configurationFile:add(value=/opt/jboss/wildfly/standalone/configuration/log4j2.xml)" && \
$JBOSS_HOME/bin/jboss-cli.sh --connect --command=:shutdown && \
rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ $JBOSS_HOME/standalone/log/* && \
rm -rf $JBOSS_HOME/modules/system/layers/base/org/postgresql/main/postgresql-42.5.0.jar

# Expose the ports in which we're interested
EXPOSE 8080/tcp 9990/tcp 5432/tcp 8787/tcp

COPY target/*.war $JBOSS_HOME/standalone/deployments/

# Set the default command to run on boot
# This will boot WildFly in standalone mode and bind to all interfaces
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]