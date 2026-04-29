package es.ulpgc.datos;

import es.ulpgc.datos.feeder.FootballDataFeeder;
import es.ulpgc.datos.publisher.MatchEventPublisher;
import es.ulpgc.datos.store.DatabaseMatchSerializer;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller(
                new FootballDataFeeder(),
                new DatabaseMatchSerializer(),
                new MatchEventPublisher()
        );
        controller.start();
    }
}