package mars;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.impl.ActiveMQServerImpl;

public class ArtemisServer {

  private ActiveMQServer activeMQServer;

  private void configureAndStartServer() throws Exception {
    Configuration configuration = createArtemisConfig();
    activeMQServer = new ActiveMQServerImpl(configuration);
    activeMQServer.start();
  }

  /**
   * 创建configuration
   */
  private Configuration createArtemisConfig() {
    // Step 1. Create Apache ActiveMQ Artemis core configuration, and set the properties accordingly
    Configuration configuration = new ConfigurationImpl();
    configuration.setPersistenceEnabled(false);
    configuration.setSecurityEnabled(false);
    configuration.setJournalDirectory("journal");
    configuration.setBindingsDirectory("bindings");
    configuration.setLargeMessagesDirectory("large-messages");
    configuration.getAcceptorConfigurations()
            .add(new TransportConfiguration(NettyAcceptorFactory.class.getName()));
    return configuration;
  }

  public static void main(String[] args) throws Exception {
    ArtemisServer artemisServer = new ArtemisServer();
    artemisServer.configureAndStartServer();
  }

}
