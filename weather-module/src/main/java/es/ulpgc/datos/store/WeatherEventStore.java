package es.ulpgc.datos.store;

import com.google.gson.Gson;
import es.ulpgc.datos.model.WeatherEvent;
import org.apache.activemq.ActiveMQConnectionFactory;

import jakarta.jms.*;

public class WeatherEventStore {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "Weather";

    private final Gson gson = new Gson();

    public void publish(WeatherEvent event) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(TOPIC_NAME);
            MessageProducer producer = session.createProducer(destination);

            String json = gson.toJson(event);
            TextMessage message = session.createTextMessage(json);
            producer.send(message);

            System.out.println("Evento publicado: " + json);

            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            System.err.println("Error al publicar evento en ActiveMQ: " + e.getMessage());
        }
    }
}