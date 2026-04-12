package es.ulpgc.datos.feeder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import es.ulpgc.datos.model.Weather;
import es.ulpgc.datos.model.WeatherMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapFeeder implements WeatherFeeder {

    private static final String API_KEY = "0ac1d3838cfb84d1f1a6ae57fc7d91e7";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    private static final List<String> CITIES = List.of(
            "Madrid", "Barcelona", "Seville", "Valencia", "Bilbao",
            "Las Palmas de Gran Canaria", "Zaragoza", "Malaga"
    );

    private final WeatherMapper mapper = new WeatherMapper();

    @Override
    public List<Weather> fetchWeather() {
        List<Weather> results = new ArrayList<>();

        for (String city : CITIES) {
            try {
                String json = fetchJson(city);
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                results.add(mapper.map(jsonObject));
            } catch (IOException | InterruptedException e) {
                System.err.println("Error al obtener datos de " + city + ": " + e.getMessage());
            }
        }

        return results;
    }

    private String fetchJson(String city) throws IOException, InterruptedException {
        String url = String.format(BASE_URL, city.replace(" ", "%20"), API_KEY);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}
