package es.ulpgc.datos;

import es.ulpgc.datos.feeder.FootballDataFeeder;
import es.ulpgc.datos.publisher.MatchEventPublisher;
import es.ulpgc.datos.storer.DatabaseMatchSerializer;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: Main <api-url> <database-name>");
            return;
        }

        String apiUrl = args[0];
        String databaseName = args[1];

        Controller controller = new Controller(
                new FootballDataFeeder(apiUrl),
                new DatabaseMatchSerializer(databaseName),
                new MatchEventPublisher()
        );
        controller.start();
    }
}