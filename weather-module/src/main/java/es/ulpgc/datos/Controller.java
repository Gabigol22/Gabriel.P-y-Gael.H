package es.ulpgc.datos;

import es.ulpgc.datos.feeder.WeatherEventPublisher;
import es.ulpgc.datos.feeder.WeatherFeeder;
import es.ulpgc.datos.model.Weather;
import es.ulpgc.datos.model.WeatherEvent;
import es.ulpgc.datos.storer.WeatherStore;

import java.time.Instant;
import java.util.List;

public class Controller {

    private final WeatherFeeder feeder;
    private final WeatherStore serializer;
    private final WeatherEventPublisher publisher;

    public Controller(WeatherFeeder feeder, WeatherStore serializer) {
        this.feeder = feeder;
        this.serializer = serializer;
        this.publisher = new WeatherEventPublisher();
    }

    public void start() {
        System.out.println("Obteniendo datos meteorológicos...");
        List<Weather> weatherList = feeder.fetchWeather();

        System.out.println("Ciudades obtenidas: " + weatherList.size());
        weatherList.forEach(System.out::println);

        System.out.println("Guardando en base de datos...");
        serializer.serialize(weatherList);

        System.out.println("Publicando eventos en ActiveMQ...");
        for (Weather weather : weatherList) {
            WeatherEvent event = new WeatherEvent(
                    Instant.now().toString(),
                    "OpenWeatherMap",
                    weather.getCity(),
                    weather.getCountry(),
                    weather.getTemperature(),
                    weather.getFeelsLike(),
                    weather.getHumidity(),
                    weather.getDescription()
            );
            publisher.publish(event);
        }
    }
}
