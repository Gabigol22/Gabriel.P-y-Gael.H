package es.ulpgc.datos.storer;

import es.ulpgc.datos.model.Match;
import java.util.List;

public interface MatchStore {

    void serialize(List<Match> matches);

}