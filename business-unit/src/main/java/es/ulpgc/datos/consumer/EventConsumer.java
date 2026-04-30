package es.ulpgc.datos.consumer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import es.ulpgc.datos.datamart.Datamart;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class EventConsumer {

    private static final String CLIENT_ID = "business-unit-" + System.currentTimeMillis();
    private final String brokerUrl;
    private final Datamart datamart;

    public EventConsumer(String brokerUrl, Datamart datamart) {
        this.brokerUrl = "failover:(" + brokerUrl + ")?maxReconnectAttempts=10&initialReconnectDelay=1000";
        this.datamart = datamart;
    }

    public void subscribe(String topicName) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
                    Connection connection = factory.createConnection();
                    connection.setClientID(CLIENT_ID + "_" + topicName);
                    connection.start();

                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Topic topic = session.createTopic(topicName);
                    MessageConsumer consumer = session.createDurableSubscriber(topic, "bu-sub-" + topicName);

                    System.out.println("Business Unit suscrito al topic: " + topicName);

                    while (true) {
                        Message message = consumer.receive(1000);
                        if (message instanceof TextMessage textMessage) {
                            String json = textMessage.getText();
                            JsonObject event = JsonParser.parseString(json).getAsJsonObject();
                            processEvent(topicName, event);
                        }
                    }

                } catch (JMSException e) {
                    System.err.println("Error en " + topicName + ": " + e.getMessage());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        thread.setDaemon(false);
        thread.start();
    }

    private void processEvent(String topic, JsonObject event) {
        if (topic.equals("Football")) {
            String homeTeam = event.get("homeTeam").getAsString();
            String awayTeam = event.get("awayTeam").getAsString();
            String matchDate = event.get("matchDate").getAsString();
            String ts = event.get("ts").getAsString();
            String city = getCityForTeam(homeTeam);
            datamart.insertMatchWeather(homeTeam, awayTeam, matchDate, city, 0, 0, "N/A", ts);
        } else if (topic.equals("Weather")) {
            System.out.println("Evento de tiempo recibido: " + event.get("city").getAsString());
        }
    }

    private String getCityForTeam(String team) {
        return switch (team) {
            case "Real Madrid CF" -> "Madrid";
            case "FC Barcelona" -> "Barcelona";
            case "Sevilla FC" -> "Seville";
            case "Valencia CF" -> "Valencia";
            case "Athletic Club" -> "Bilbao";
            case "Girona FC" -> "Girona";
            case "CA Osasuna" -> "Pamplona";
            case "RCD Mallorca" -> "Palma";
            case "Club Atlético de Madrid" -> "Madrid";
            case "Real Sociedad de Fútbol" -> "San Sebastian";
            case "Villarreal CF" -> "Villarreal";
            case "Real Betis Balompié" -> "Seville";
            case "RC Celta de Vigo" -> "Vigo";
            case "Getafe CF" -> "Madrid";
            case "Rayo Vallecano de Madrid" -> "Madrid";
            case "RCD Espanyol de Barcelona" -> "Barcelona";
            case "Deportivo Alavés" -> "Vitoria";
            case "Levante UD" -> "Valencia";
            case "Elche CF" -> "Elche";
            case "Real Oviedo" -> "Oviedo";
            default -> "Unknown";
        };
    }
}