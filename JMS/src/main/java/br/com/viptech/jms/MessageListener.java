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
public class MessageListener {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext(createProps());
            
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection con = factory.createConnection();
        con.start();
        
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("financeiro");
        MessageConsumer consumer = session.createConsumer(fila);
        
        consumer.setMessageListener(new javax.jms.MessageListener() {
            @Override
            public void onMessage(Message msg) {
                
                TextMessage text = (TextMessage) msg;
                
                try {
                    System.out.println(text.getText());
                } catch (JMSException ex) {
                    Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } );
        
        new Scanner(System.in).nextLine(); //parar o programa para testar a conexao
        
        session.close();

        con.close();
        context.close();
    }
    
    private static Properties createProps(){
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.put("java.naming.provider.url", "tcp://localhost:61616");        
        properties.put("queue.financeiro", "fila.financeiro");
        return properties;
    } 
    
}
