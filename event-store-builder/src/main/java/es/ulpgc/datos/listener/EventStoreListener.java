package es.ulpgc.datos.listener;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import es.ulpgc.datos.storer.EventStore;
import org.apache.activemq.ActiveMQConnectionFactory;

import jakarta.jms.*;

public class EventStoreListener {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private final EventStore eventStore = new EventStore();

    public void subscribe(String topicName) {
        Thread thread = new Thread(() -> {
            try {
                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
                Connection connection = factory.createConnection();
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic topic = session.createTopic(topicName);
                MessageConsumer consumer = session.createConsumer(topic);

                System.out.println("Suscrito al topic: " + topicName);

                while (true) {
                    Message message = consumer.receive(1000);
                    if (message instanceof TextMessage textMessage) {
                        String json = textMessage.getText();
                        JsonObject event = JsonParser.parseString(json).getAsJsonObject();
                        String ts = event.get("ts").getAsString();
                        String ss = event.get("ss").getAsString();
                        eventStore.store(topicName, ss, ts, json);
                    }
                }

            } catch (JMSException e) {
                System.err.println("Error en " + topicName + ": " + e.getMessage());
            }
        });
        thread.setDaemon(false);
        thread.start();
    }
}