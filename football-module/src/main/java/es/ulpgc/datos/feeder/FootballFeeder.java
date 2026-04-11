package es.ulpgc.datos.feeder;

import es.ulpgc.datos.model.Match;
import java.util.List;

public interface FootballFeeder {

    List<Match> fetchMatches();

}