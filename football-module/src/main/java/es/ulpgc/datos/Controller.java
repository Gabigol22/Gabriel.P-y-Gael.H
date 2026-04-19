package es.ulpgc.datos;

import es.ulpgc.datos.feeder.FootballFeeder;
import es.ulpgc.datos.model.Match;
import es.ulpgc.datos.storer.MatchSerializer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private final FootballFeeder feeder;
    private final MatchSerializer serializer;

    public Controller(FootballFeeder feeder, MatchSerializer serializer) {
        this.feeder = feeder;
        this.serializer = serializer;
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::fetch, 0, 1, TimeUnit.HOURS);
        System.out.println("Scheduler iniciado. Capturando datos cada hora...");
    }

    private void fetch() {
        System.out.println("Obteniendo partidos de LaLiga...");
        List<Match> matches = feeder.fetchMatches();
        System.out.println("Partidos obtenidos: " + matches.size());
        matches.forEach(System.out::println);
        System.out.println("Guardando en base de datos...");
        serializer.serialize(matches);
    }
}