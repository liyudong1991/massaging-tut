package galaxy.raccoon.app;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 和hello中不同的是, 创建的消息指定可以用于过滤的条件
 */
public class Producer {

  // 单例模式

  private ConnectionFactory factory;
  private Connection connection;
  private Session session;
  private MessageProducer producer;

  public Producer() {
    try {
      this.factory = new ActiveMQConnectionFactory(
              "test",
              "test",
              "tcp://localhost:61616"
      );
      this.connection = this.factory.createConnection();
      this.connection.start();
      this.session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
      this.producer = this.session.createProducer(null);
    } catch (JMSException e) {
      e.printStackTrace();
    }

  }

  public Session getSession() {
    return this.session;
  }

  public void send1() {
    try {
      Destination destination = this.session.createQueue("first");
      MapMessage msg1 = this.session.createMapMessage();
      msg1.setString("name", "一一");
      msg1.setString("age", "23");
      msg1.setStringProperty("color", "blue");
      msg1.setIntProperty("sal", 3000);
      int id = 1;
      msg1.setInt("id", id);
      String receiver = id % 2 == 0 ? "A" : "B";
      msg1.setStringProperty("receiver", receiver);

      MapMessage msg2 = this.session.createMapMessage();
      msg2.setString("name", "二二");
      msg2.setString("age", "24");
      msg2.setStringProperty("color", "red");
      msg2.setIntProperty("sal", 1300);
      id = 2;
      msg2.setInt("id", id);
      receiver = id % 2 == 0 ? "A" : "B";
      msg2.setStringProperty("receiver", receiver);

      MapMessage msg3 = this.session.createMapMessage();
      msg3.setString("name", "三三");
      msg3.setString("age", "22");
      msg3.setStringProperty("color", "pink");
      msg3.setIntProperty("sal", 1400);
      id = 2;
      msg3.setInt("id", id);
      receiver = id % 2 == 0 ? "A" : "B";
      msg3.setStringProperty("receiver", receiver);

      this.producer.send(destination, msg1, DeliveryMode.PERSISTENT, 2, 1000 * 60 * 10L);
      this.producer.send(destination, msg2, DeliveryMode.PERSISTENT, 6, 1000 * 60 * 10L);
      this.producer.send(destination, msg3, DeliveryMode.NON_PERSISTENT, 9, 1000 * 60 * 10L);
    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

  public void send2() {
    try {
      Destination destination = this.session.createQueue("first");
      TextMessage message = this.session.createTextMessage("这是一串字符串");
      this.producer.send(destination, message, DeliveryMode.PERSISTENT, 9, 1000 * 60 * 10L);
    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Producer producer = new Producer();
    producer.send1();
//    producer.send2();
  }


}
