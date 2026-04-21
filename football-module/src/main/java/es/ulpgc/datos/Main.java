package es.ulpgc.datos;

import es.ulpgc.datos.feeder.FootballDataFeeder;
import es.ulpgc.datos.publisher.MatchEventPublisher;
import es.ulpgc.datos.store.DatabaseMatchStore;

public class Main {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Uso: Main <api-url> <api-key> <database-name> <broker-url>");
            return;
        }

        String apiUrl = args[0];
        String apiKey = args[1];
        String databaseName = args[2];
        String brokerUrl = args[3];

        Controller controller = new Controller(
                new FootballDataFeeder(apiUrl, apiKey),
                new DatabaseMatchStore(databaseName),
                new MatchEventPublisher(brokerUrl)
        );
        controller.start();
    }
}