
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class EjemploConsumidor {

    void processConsumer() {
        String clientID = "cristobal";
        try {
            ActiveMQConnectionFactory connFact = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616");
            Connection conn = connFact.createConnection();
            //conn.setExceptionListener(this);//WTF

            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("MyQueue");
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(listener);
            conn.start();
        } catch (Exception e) {
            System.out.println("Excepcion: " + e.getMessage());
            e.printStackTrace();
        }
    }
    MessageListener listener = new MessageListener() {
        public void onMessage(Message msg) {
            if (msg instanceof TextMessage) {
                TextMessage txtmsg = (TextMessage) msg;
                String text = null;
                try {
                    text = txtmsg.getText();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Recibido mensaje: " + text);
            } else {
                System.out.println("Recibido: " + msg);
            }
        }
    };

    public synchronized void onException(JMSException ex) {
        System.out.println("Ocurrio excepcion de JMS. Apagando Cliente");
    }

    public static void main(String[] args) throws Exception {
        EjemploConsumidor c = new EjemploConsumidor();
        System.out.println("Iniciando Consumidor");
        c.processConsumer();

    }
}
