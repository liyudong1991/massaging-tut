package galaxy.raccoon.hello;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Objects;

public class Receiver {

  public static void main(String[] args) throws Exception {

    // 1. 创建ConnectionFactory
    ConnectionFactory factory = new ActiveMQConnectionFactory(
            "test",
            "test",
            "tcp://localhost:61616"
    );

    // 2. 通过ConnectionFactory创建Connection, 调用start方法
    Connection connection = factory.createConnection();
    connection.start();
    // 3. 创建session (是否启用事务, 签收模式) 使用手动签收
    Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);


    // 4. 创建Destination, 指的是一个客户端用来指定生产消息
    // 目标和消费消息来源的对象, 在PTP模式中, Destination被称为
    // Queue; 在Pub/Sub模式中, 被称为Topic
    Destination destination = session.createQueue("queue1");

    // 5. 创建发送和接受对象
    MessageConsumer consumer = session.createConsumer(destination);

    while (true) {
      TextMessage message = (TextMessage) consumer.receive();
      // 手动签收 这里白鹤翔描述是另起一个tcp线程告诉mq数据签收了.
      message.acknowledge();
      if (Objects.isNull(message)) {
        //todo 这个break 似乎没有作用. 因为当queue中没有数据时, receive会阻塞.
        break;
      }
      System.out.println("接收到的消息: " + message.getText());
    }

    if (null != connection) {
      connection.close();
    }

  }
}
