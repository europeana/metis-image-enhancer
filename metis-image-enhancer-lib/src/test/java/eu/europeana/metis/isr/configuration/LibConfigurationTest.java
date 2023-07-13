package eu.europeana.metis.isr.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LibConfigurationTest {

    LibConfiguration libConfiguration = new LibConfiguration();

    @Test
    void apiUrl() {
        assertEquals("http://localhost:5050", libConfiguration.getApiUrl());
    }

    @Test
    void connectTimeout() {
        assertEquals(300, libConfiguration.getConnectTimeout());
    }

    @Test
    void readTimeout() {
        assertEquals(300, libConfiguration.getReadTimeout());
    }

}
