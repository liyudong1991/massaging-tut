package mars.app;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 不使用while true的方式; 监听器.
 */
public class Consumer {

  public final String SELECTOR_1 = "color = 'blue'";
  public final String SELECTOR_2 = "color = 'blue' and sal > 2000";
  public final String SELECTOR_3 = "receiver = 'A'";

  private ConnectionFactory factory;
  private Connection connection;
  private Session session;
  private MessageConsumer consumer;
  private Destination destination;

  public Consumer(){
    try {
      this.factory = new ActiveMQConnectionFactory(
              "test",
              "test",
              "tcp://localhost:61616"
      );
      this.connection = this.factory.createConnection();
      this.connection.start();
      this.session = this.connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
      this.destination = this.session.createQueue("first");
      // selector 是个过滤语法, 语法规则遵守sql 92, 目的是筛选出destination中符合条件的数据进行消费.
      this.consumer = this.session.createConsumer(this.destination, SELECTOR_3);

    } catch (JMSException e) {
      e.printStackTrace();
    }

  }

  /**
   * todo activemq 监听器原理
   */
  public void receiver(){
    try {
      this.consumer.setMessageListener(message -> {
        // message是被观察者
        try {
          if (message instanceof TextMessage){

          }
          if (message instanceof MapMessage){
            MapMessage mapMessage = (MapMessage) message;
            System.out.println(mapMessage.toString());
            System.out.println(mapMessage.getString("name"));
            System.out.println(mapMessage.getString("age"));
          }
        } catch (JMSException e) {
          e.printStackTrace();
        }
      });
    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Consumer consumer = new Consumer();
    consumer.receiver();
  }


}

