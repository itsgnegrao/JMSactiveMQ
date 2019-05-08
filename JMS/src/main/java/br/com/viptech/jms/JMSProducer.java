package br.com.viptech.jms;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import javax.naming.InitialContext;

/**
 *
 * @author gnsilva@viptech.com.br
 */
public class JMSProducer {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext(createProps());

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection con = factory.createConnection();
        con.start();

        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("financeiro");
        MessageProducer producer = session.createProducer(fila);

        for (int i = 0; i < 1000; i++) {
            Message message = session.createTextMessage("Batatatas " + i);
            producer.send(message);
        }

        new Scanner(System.in).nextLine(); //parar o programa para testar a conexao

        session.close();

        con.close();
        context.close();
    }

    private static Properties createProps() {
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.put("java.naming.provider.url", "tcp://localhost:61616");
        properties.put("queue.financeiro", "fila.financeiro");
        return properties;
    }

}
