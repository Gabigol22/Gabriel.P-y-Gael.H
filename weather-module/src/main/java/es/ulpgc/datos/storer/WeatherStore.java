package es.ulpgc.datos.storer;

import es.ulpgc.datos.model.Weather;
import java.util.List;

public interface WeatherStore {

    void serialize(List<Weather> weatherList);

}