package mars.example;

import javax.jms.*;
import javax.naming.InitialContext;

/**
 *
 */
public class Producer {


  public static void main(String[] args) throws Exception {
    // 2.2.0版本与2.5.0版本创建connectionFactory的方式不同
    InitialContext ic = new InitialContext();
    //Now we'll look up the connection factory from which we can create
    //connections to myhost:5445:
    ConnectionFactory cf = (ConnectionFactory) ic.lookup("ConnectionFactory");
    //And look up the Queue:
    Queue orderQueue = (Queue) ic.lookup("queues/OrderQueue");
    //Next we create a JMS connection using the connection factory:
    Connection connection = cf.createConnection();
    //And we create a non transacted JMS Session, with AUTO\_ACKNOWLEDGE
    //acknowledge mode:
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    //We create a MessageProducer that will send orders to the queue:
    MessageProducer producer = session.createProducer(orderQueue);
    //And we create a MessageConsumer which will consume orders from the
    //queue:
    MessageConsumer consumer = session.createConsumer(orderQueue);
    //We make sure we start the connection, or delivery won't occur on it:
    connection.start();
    //We create a simple TextMessage and send it:
    TextMessage message = session.createTextMessage("This is an order");
    producer.send(message);
    //And we consume the message:
    TextMessage receivedMessage = (TextMessage) consumer.receive();
    System.out.println("Got order: " + receivedMessage.getText());
  }
}
