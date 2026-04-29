package es.ulpgc.datos.store;

import es.ulpgc.datos.model.Weather;
import java.util.List;

public interface WeatherStore {

    void store(List<Weather> weatherList);

}