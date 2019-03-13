package galaxy.reccon.hello;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {


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
    // 3. 创建session (是否启用事务, 签收模式)
    Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);


    // 4. 创建Destination, 指的是一个客户端用来指定生产消息
    // 目标和消费消息来源的对象, 在PTP模式中, Destination被称为
    // Queue; 在Pub/Sub模式中, 被称为Topic
    Destination destination = session.createQueue("queue1");

    // 5. 创建发送和接受对象
    MessageProducer producer = session.createProducer(destination);

    // 6. 使用MessageProducer的setDeliveryMode方法设置是否持久化
    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

    // 7. TextMessage形式创建数据, send数据, 关闭Connection连接.

    for (int i = 1; i <= 5; i++) {

      TextMessage message = session.createTextMessage();
      message.setText("第" + i + "条消息内容");
      producer.send(message);
      System.out.println("发送的消息: "+message.getText());
    }

    if(null != connection){
      connection.close();
    }

  }

}
