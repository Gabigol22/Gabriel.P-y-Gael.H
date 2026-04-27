package es.ulpgc.datos;

import es.ulpgc.datos.feeder.OpenWeatherMapFeeder;
import es.ulpgc.datos.storer.DatabaseWeatherStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java Main <api_url> <database_path>");
            System.err.println("Ejemplo: java Main \"https://api.openweathermap.org/data/2.5/weather?q=%s&appid=TU_KEY&units=metric\" weather.db");
            System.exit(1);
        }

        String apiUrl = args[0];
        String dbPath = args[1];

        Controller controller = new Controller(
                new OpenWeatherMapFeeder(apiUrl),
                new DatabaseWeatherStore(dbPath)
        );

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(controller::start, 0, 1, TimeUnit.HOURS);

        System.out.println("Scheduler iniciado. Capturando datos cada hora...");
    }
}