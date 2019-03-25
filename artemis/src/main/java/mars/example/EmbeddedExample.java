package mars.example;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.jms.server.config.ConnectionFactoryConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSQueueConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;

import javax.jms.*;
import java.util.Arrays;
import java.util.Date;

public class EmbeddedExample {

  public static void main(final String[] args) throws Exception {

    // Step 1. Create ActiveMQ Artemis core configuration, and set the properties accordingly
    Configuration configuration = new ConfigurationImpl()
            .setPersistenceEnabled(false)
            .setJournalDirectory("target/data/journal")
            .setSecurityEnabled(false)
            .addAcceptorConfiguration(new TransportConfiguration(NettyAcceptorFactory.class.getName()))
            .addConnectorConfiguration("connector", new TransportConfiguration(NettyConnectorFactory.class.getName()));

    // Step 2. Create the JMS configuration
    JMSConfiguration jmsConfig = new JMSConfigurationImpl();

    // Step 3. Configure the JMS ConnectionFactory
    ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl()
            .setName("cf")
            .setConnectorNames(Arrays.asList("connector"))
            .setBindings("cf");
    jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);

    // Step 4. Configure the JMS Queue
    JMSQueueConfiguration queueConfig = new JMSQueueConfigurationImpl()
            .setName("queue1")
            .setDurable(false)
            .setBindings("queue/queue1");
    jmsConfig.getQueueConfigurations().add(queueConfig);

    // Step 5. Start the JMS Server using the ActiveMQ Artemis core server and the JMS configuration
    EmbeddedJMS jmsServer = new EmbeddedJMS()
            .setConfiguration(configuration)
            .setJmsConfiguration(jmsConfig)
            .start();
    System.out.println("Started Embedded JMS Server");

    // Step 6. Lookup JMS resources defined in the configuration
    ConnectionFactory cf = (ConnectionFactory) jmsServer.lookup("cf");
    Queue queue = (Queue) jmsServer.lookup("queue/queue1");

    // Step 7. Send and receive a message using JMS API
    Connection connection = null;
    try {
      connection = cf.createConnection();
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer producer = session.createProducer(queue);
      TextMessage message = session.createTextMessage("Hello sent at(发送消息) " + new Date());
      System.out.println("Sending message: " + message.getText());
      producer.send(message);
      MessageConsumer messageConsumer = session.createConsumer(queue);
      connection.start();
      // 获取到message类型
      TextMessage messageReceived = (TextMessage) messageConsumer.receive(1000);
      System.out.println("Received message:" + messageReceived.getText());

    } finally {

      if (connection != null) {
        connection.close();
      }

      // Step 11. Stop the JMS server
      jmsServer.stop();
      System.out.println("Stopped the JMS Server");

    }
  }
}
