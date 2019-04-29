package config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.stereotype.Component;

@Component
public class TomcatConfigFactory extends TomcatEmbeddedServletContainerFactory {

    @Value("${env_prod}")
    private boolean envProd;

    @Override
    protected void customizeConnector(Connector connector) {
        super.customizeConnector(connector);
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        protocol.setConnectionTimeout(30000);
        protocol.setMaxKeepAliveRequests(1);
        protocol.setKeepAliveTimeout(0);
        protocol.setCompression("off");
        protocol.setNoCompressionUserAgents("");
        if (envProd) {
            protocol.setMaxConnections(2000);
            protocol.setMaxThreads(2000);
            protocol.setAcceptCount(5000);
        } else {
            protocol.setMaxConnections(100);
            protocol.setMaxThreads(50);
            protocol.setAcceptCount(50);
        }
    }
}
