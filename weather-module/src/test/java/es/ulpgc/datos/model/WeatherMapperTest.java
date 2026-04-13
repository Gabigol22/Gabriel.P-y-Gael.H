package es.ulpgc.datos.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeatherMapperTest {

    private final WeatherMapper mapper = new WeatherMapper();

    private JsonObject sampleWeatherJson() {
        String json = """
        {
            "name": "Madrid",
            "sys": {"country": "ES"},
            "main": {
                "temp": 10.79,
                "feels_like": 9.5,
                "humidity": 45
            },
            "weather": [
                {"description": "clear sky"}
            ]
        }
        """;
        return JsonParser.parseString(json).getAsJsonObject();
    }

    @Test
    void mapReturnsCorrectCity() {
        Weather weather = mapper.map(sampleWeatherJson());
        assertEquals("Madrid", weather.getCity());
    }

    @Test
    void mapReturnsCorrectTemperature() {
        Weather weather = mapper.map(sampleWeatherJson());
        assertEquals(10.79, weather.getTemperature());
    }

    @Test
    void mapReturnsCorrectHumidity() {
        Weather weather = mapper.map(sampleWeatherJson());
        assertEquals(45, weather.getHumidity());
    }

    @Test
    void mapReturnsCorrectDescription() {
        Weather weather = mapper.map(sampleWeatherJson());
        assertEquals("clear sky", weather.getDescription());
    }

    @Test
    void mapReturnsCorrectCountry() {
        Weather weather = mapper.map(sampleWeatherJson());
        assertEquals("ES", weather.getCountry());
    }

    @Test
    void mapSetsCapturedAtNotNull() {
        Weather weather = mapper.map(sampleWeatherJson());
        assertNotNull(weather.getCapturedAt());
    }
}
