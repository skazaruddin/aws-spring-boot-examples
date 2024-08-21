package io.azar.examples.holyquran.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "api")
@Data
public class ApiConfigurationProperties {

    private QuranApi quranapi; // Reference to QuranApi properties

    @Data
    public static class QuranApi {
        private String host;
        private Truststore truststore;
        private Connection connection;
    }
    @Data
    public static class Truststore {
        private List<String> ciphers;
        private String certificates;
    }
    @Data
    public static class Connection {
        private int connectTimeoutSeconds; // Updated connect timeout property
        private int readTimeoutSeconds; // Updated read timeout property
        private int writeTimeoutSeconds; // Updated write timeout property
        private boolean keepAlive; // New property for keep-alive
        private boolean tcpNoDelay; // New property for TCP no delay
    }
}

