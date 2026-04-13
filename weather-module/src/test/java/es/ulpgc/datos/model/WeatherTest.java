package es.ulpgc.datos.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeatherTest {

    private Weather createSampleWeather() {
        return new Weather("Madrid", 10.79, 9.5, 45, "clear sky", "ES");
    }

    @Test
    void weatherStoresCityCorrectly() {
        assertEquals("Madrid", createSampleWeather().getCity());
    }

    @Test
    void weatherStoresTemperatureCorrectly() {
        assertEquals(10.79, createSampleWeather().getTemperature());
    }

    @Test
    void weatherStoresHumidityCorrectly() {
        assertEquals(45, createSampleWeather().getHumidity());
    }

    @Test
    void weatherCapturedAtIsSetAutomatically() {
        assertNotNull(createSampleWeather().getCapturedAt());
    }

    @Test
    void toStringContainsCityName() {
        assertTrue(createSampleWeather().toString().contains("Madrid"));
    }
}
