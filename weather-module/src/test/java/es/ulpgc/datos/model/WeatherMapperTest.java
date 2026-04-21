package es.ulpgc.datos.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeatherMapperTest {

    private final WeatherMapper mapper = new WeatherMapper();

    private JsonObject sampleJson() {
        String json = """
            {
                "name": "Madrid",
                "sys": { "country": "ES" },
                "main": {
                    "temp": 20.5,
                    "feels_like": 19.0,
                    "humidity": 60
                },
                "weather": [
                    { "description": "clear sky" }
                ]
            }
            """;
        return JsonParser.parseString(json).getAsJsonObject();
    }

    @Test
    void mapsCityCorrectly() {
        assertEquals("Madrid", mapper.map(sampleJson()).getCity());
    }

    @Test
    void mapsCountryCorrectly() {
        assertEquals("ES", mapper.map(sampleJson()).getCountry());
    }

    @Test
    void mapsTemperatureCorrectly() {
        assertEquals(20.5, mapper.map(sampleJson()).getTemperature());
    }

    @Test
    void mapsHumidityCorrectly() {
        assertEquals(60, mapper.map(sampleJson()).getHumidity());
    }

    @Test
    void mapsDescriptionCorrectly() {
        assertEquals("clear sky", mapper.map(sampleJson()).getDescription());
    }
}