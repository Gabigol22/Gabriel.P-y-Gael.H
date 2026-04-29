package es.ulpgc.datos;

import es.ulpgc.datos.feeder.FootballDataFeeder;
import es.ulpgc.datos.store.CompositeMatchStore;
import es.ulpgc.datos.store.DatabaseMatchStore;
import es.ulpgc.datos.store.MatchEventStore;

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
                new CompositeMatchStore(
                        new DatabaseMatchStore(databaseName),
                        new MatchEventStore(brokerUrl)
                )
        );
        controller.start();
    }
}