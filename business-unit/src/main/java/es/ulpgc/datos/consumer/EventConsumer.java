package es.ulpgc.datos.consumer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import es.ulpgc.datos.datamart.Datamart;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class EventConsumer {
    private static final String CLIENT_ID = "business-unit-static-v1";
    private final String brokerUrl;
    private final Datamart datamart;

    public EventConsumer(String brokerUrl, Datamart datamart) {
        this.brokerUrl = "failover:(" + brokerUrl + ")?maxReconnectAttempts=10";
        this.datamart = datamart;
    }

    public void subscribe(String topicName) {
        new Thread(() -> {
            while (true) {
                try {
                    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
                    Connection connection = factory.createConnection();
                    connection.setClientID(CLIENT_ID + "_" + topicName);
                    connection.start();

                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Topic topic = session.createTopic(topicName);
                    MessageConsumer consumer = session.createDurableSubscriber(topic, "sub-" + topicName);

                    while (true) {
                        Message message = consumer.receive();
                        if (message instanceof TextMessage textMessage) {
                            try {
                                JsonObject event = JsonParser.parseString(textMessage.getText()).getAsJsonObject();
                                processEvent(topicName, event);
                            } catch (Exception e) {
                                System.err.println("Error procesando mensaje: " + e.getMessage());
                            }
                        }
                    }
                } catch (JMSException e) {
                    try { Thread.sleep(5000); } catch (InterruptedException ie) { break; }
                }
            }
        }).start();
    }

    private void processEvent(String topic, JsonObject event) {
        if (topic.equals("Football")) {
            String home = event.get("homeTeam").getAsString();
            String date = event.get("matchDate").toString().replace("\"", "");
            datamart.insertMatchWeather(
                    home,
                    event.get("awayTeam").getAsString(),
                    event.get("homeScore").getAsInt(),
                    event.get("awayScore").getAsInt(),
                    date,
                    getCityForTeam(home),
                    0, 0, "N/A", date
            );
        } else if (topic.equals("Weather")) {
            datamart.updateWeather(
                    event.get("city").getAsString(),
                    event.get("temperature").getAsDouble(),
                    event.get("humidity").getAsInt(),
                    event.get("description").getAsString()
            );
        }
    }

    private String getCityForTeam(String team) {
        return switch (team) {
            case "Real Madrid CF", "Club Atlético de Madrid", "Getafe CF", "Rayo Vallecano de Madrid" -> "Madrid";
            case "FC Barcelona", "RCD Espanyol de Barcelona" -> "Barcelona";
            case "Sevilla FC", "Real Betis Balompié" -> "Seville";
            case "Valencia CF", "Levante UD" -> "Valencia";
            case "Athletic Club" -> "Bilbao";
            case "Girona FC" -> "Girona";
            case "CA Osasuna" -> "Pamplona";
            case "RCD Mallorca" -> "Palma";
            case "Real Sociedad de Fútbol" -> "San Sebastian";
            case "Villarreal CF" -> "Villarreal";
            case "RC Celta de Vigo" -> "Vigo";
            case "Deportivo Alavés" -> "Vitoria";
            case "Elche CF" -> "Elche";
            case "Real Oviedo" -> "Oviedo";
            default -> "Unknown";
        };
    }
}