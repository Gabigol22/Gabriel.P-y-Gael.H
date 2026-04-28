package es.ulpgc.datos.store;

import es.ulpgc.datos.model.Weather;
import java.util.List;

public interface WeatherStore {

    void serialize(List<Weather> weatherList);

}