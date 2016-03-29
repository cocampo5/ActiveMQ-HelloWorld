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

public class EjemploProductor implements ExceptionListener {

    void processProducer() {
        try {
            ActiveMQConnectionFactory connFact = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616");
            Connection conn = connFact.createConnection();
            conn.start();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("MyQueue");
            MessageProducer prod = session.createProducer(destination);
            int i = 0;
            while (i < 20) {
                String text = "Este es el mensaje # " + i;
                TextMessage message = session.createTextMessage(text);
                prod.send(message);
                i++;
            }
            session.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Excepccion: ");
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("Ocurrio excepcion. Apagando cliente.");
    }

    public static void main(String[] args) throws Exception {
        EjemploProductor p = new EjemploProductor();
        System.out.println("Iniciando productor");
        p.processProducer();
    }
}
