package es.ulpgc.datos.publisher;

import com.google.gson.Gson;
import es.ulpgc.datos.model.Match;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.time.Instant;
import java.util.List;

public class MatchEventPublisher {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "Football";
    private static final String SOURCE_ID = "football-feeder";

    private final Gson gson = new Gson();

    public void publish(List<Match> matches) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(TOPIC_NAME);
            MessageProducer producer = session.createProducer(destination);

            for (Match match : matches) {
                String json = buildEvent(match);
                TextMessage message = session.createTextMessage(json);
                producer.send(message);
            }

            System.out.println("Publicados " + matches.size() + " eventos en el topic " + TOPIC_NAME);

            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            System.err.println("Error al publicar en ActiveMQ: " + e.getMessage());
        }
    }

    private String buildEvent(Match match) {
        MatchEvent event = new MatchEvent(
                match.getMatchDate().toString() + "Z",
                SOURCE_ID,
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getHomeScore(),
                match.getAwayScore(),
                match.getStatus(),
                match.getCompetition(),
                match.getMatchDate().toString()
        );
        return gson.toJson(event);
    }

    private record MatchEvent(
            String ts,
            String ss,
            String homeTeam,
            String awayTeam,
            int homeScore,
            int awayScore,
            String status,
            String competition,
            String matchDate
    ) {}
}