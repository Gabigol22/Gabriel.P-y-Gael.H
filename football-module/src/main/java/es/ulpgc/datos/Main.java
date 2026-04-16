package es.ulpgc.datos;

import es.ulpgc.datos.feeder.FootballDataFeeder;
import es.ulpgc.datos.storer.DatabaseMatchSerializer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller(
                new FootballDataFeeder(),
                new DatabaseMatchSerializer()
        );

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(
                controller::start,
                0,
                1,
                TimeUnit.HOURS
        );

        System.out.println("Scheduler iniciado. Capturando datos cada hora...");
    }
}
