package es.ulpgc.datos;

import es.ulpgc.datos.feeder.FootballFeeder;
import es.ulpgc.datos.model.Match;
import es.ulpgc.datos.storer.MatchSerializer;

import java.util.List;

public class Controller {

    private final FootballFeeder feeder;
    private final MatchSerializer serializer;

    public Controller(FootballFeeder feeder, MatchSerializer serializer) {
        this.feeder = feeder;
        this.serializer = serializer;
    }

    public void start() {
        System.out.println("Obteniendo partidos de LaLiga...");
        List<Match> matches = feeder.fetchMatches();

        System.out.println("Partidos obtenidos: " + matches.size());
        matches.forEach(System.out::println);

        System.out.println("Guardando en base de datos...");
        serializer.serialize(matches);
    }
}