package es.ulpgc.datos.model;

import com.google.gson.JsonObject;

public class WeatherMapper {

    public Weather map(JsonObject json) {
        String city = json.get("name").getAsString();
        String country = json.getAsJsonObject("sys").get("country").getAsString();

        JsonObject main = json.getAsJsonObject("main");
        double temperature = main.get("temp").getAsDouble();
        double feelsLike = main.get("feels_like").getAsDouble();
        int humidity = main.get("humidity").getAsInt();

        String description = json.getAsJsonArray("weather")
                .get(0).getAsJsonObject()
                .get("description").getAsString();

        return new Weather(city, temperature, feelsLike, humidity, description, country);
    }
}
