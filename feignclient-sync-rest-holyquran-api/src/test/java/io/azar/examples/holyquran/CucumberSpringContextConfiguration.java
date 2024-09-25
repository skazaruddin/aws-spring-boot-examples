package io.azar.examples.holyquran;

import io.cucumber.spring.CucumberContextConfiguration;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@ActiveProfiles({"unit-test"})
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
    "api.quranapi.host=http://localhost:${wiremock.server.port}"
})
class CucumberSpringContextConfiguration {

    @LocalServerPort
    private int port;

    @PostConstruct
    public void setup() {
        System.setProperty("port", String.valueOf(port));
    }

}
