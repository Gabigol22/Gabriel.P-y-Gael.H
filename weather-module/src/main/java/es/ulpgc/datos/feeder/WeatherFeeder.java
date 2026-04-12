package es.ulpgc.datos.feeder;

import es.ulpgc.datos.model.Weather;
import java.util.List;

public interface WeatherFeeder {

    List<Weather> fetchWeather();

}