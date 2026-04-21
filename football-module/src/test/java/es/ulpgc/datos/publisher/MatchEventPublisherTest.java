package es.ulpgc.datos.publisher;

import es.ulpgc.datos.model.Match;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchEventPublisherTest {

    @Test
    void publishDoesNotThrowWithEmptyList() {
        MatchEventPublisher publisher = new MatchEventPublisher("tcp://localhost:61616");
        assertDoesNotThrow(() -> publisher.publish(List.of()));
    }

    @Test
    void publishDoesNotThrowWithValidMatches() {
        MatchEventPublisher publisher = new MatchEventPublisher();
        Match match = new Match("Real Madrid CF", "FC Barcelona", 3, 2,
                "FINISHED", "Primera Division", LocalDateTime.of(2026, 3, 22, 20, 0));

        assertDoesNotThrow(() -> publisher.publish(List.of(match)));
    }
}