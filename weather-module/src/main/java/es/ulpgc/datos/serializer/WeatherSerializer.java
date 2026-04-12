package es.ulpgc.datos.serializer;

import es.ulpgc.datos.model.Weather;
import java.util.List;

public interface WeatherSerializer {

    void serialize(List<Weather> weatherList);

}