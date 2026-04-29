package es.ulpgc.datos;

import es.ulpgc.datos.feeder.OpenWeatherMapFeeder;
import es.ulpgc.datos.store.CompositeWeatherStore;
import es.ulpgc.datos.store.DatabaseWeatherStore;
import es.ulpgc.datos.store.WeatherEventStore;

public class Main {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Uso: Main <api-url> <database-name> <broker-url>");
            return;
        }

        String apiUrl = args[0];
        String databaseName = args[1];
        String brokerUrl = args[2];

        Controller controller = new Controller(
                new OpenWeatherMapFeeder(apiUrl),
                new CompositeWeatherStore(
                        new DatabaseWeatherStore(databaseName),
                        new WeatherEventStore(brokerUrl)
                )
        );
        controller.start();
    }
}