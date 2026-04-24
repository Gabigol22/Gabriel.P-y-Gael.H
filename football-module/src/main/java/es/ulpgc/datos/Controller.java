package es.ulpgc.datos;

import es.ulpgc.datos.feeder.FootballFeeder;
import es.ulpgc.datos.model.Match;
import es.ulpgc.datos.store.MatchEventStore;
import es.ulpgc.datos.store.MatchStore;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private final FootballFeeder feeder;
    // He puesto 'matchStore' y 'eventStore' para que no se llamen igual
    private final MatchStore matchStore;
    private final MatchEventStore eventStore;

    public Controller(FootballFeeder feeder, MatchStore matchStore, MatchEventStore eventStore) {
        this.feeder = feeder;
        this.matchStore = matchStore;
        this.eventStore = eventStore;
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

        // Aquí usamos los métodos 'store' que es como quieres llamarlos ahora
        System.out.println("Guardando en base de datos...");
        matchStore.store(matches);

        System.out.println("Publicando eventos en ActiveMQ...");
        eventStore.store(matches);
    }
}