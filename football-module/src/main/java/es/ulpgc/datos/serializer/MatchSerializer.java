package es.ulpgc.datos.serializer;

import es.ulpgc.datos.model.Match;
import java.util.List;

public interface MatchSerializer {

    void serialize(List<Match> matches);

}