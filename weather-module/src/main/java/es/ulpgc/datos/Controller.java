package es.ulpgc.datos;

import es.ulpgc.datos.feeder.WeatherFeeder;
import es.ulpgc.datos.model.Weather;
import es.ulpgc.datos.serializer.WeatherSerializer;

import java.util.List;

public class Controller {

    private final WeatherFeeder feeder;
    private final WeatherSerializer serializer;

    public Controller(WeatherFeeder feeder, WeatherSerializer serializer) {
        this.feeder = feeder;
        this.serializer = serializer;
    }

    public void start() {
        System.out.println("Obteniendo datos meteorológicos...");
        List<Weather> weatherList = feeder.fetchWeather();

        System.out.println("Ciudades obtenidas: " + weatherList.size());
        weatherList.forEach(System.out::println);

        System.out.println("Guardando en base de datos...");
        serializer.serialize(weatherList);
    }
}
