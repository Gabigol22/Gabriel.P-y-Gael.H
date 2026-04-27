package es.ulpgc.datos.store;

import es.ulpgc.datos.model.Match;
import java.util.List;

public interface MatchStore {

    void store(List<Match> matches);

}