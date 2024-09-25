package io.azar.examples.holyquran;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurahStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

    @Given("a chapter number of {int} and an edition identifier of {string}")
    public void a_chapter_number_and_edition_identifier(int chapter, String edition) {
        String url = String.format("/v1/surah/%d/%s", chapter, edition);
        sendGetRequest(url);
    }

    @When("the client sends a GET request to {string}")
    public void sendGetRequest(String url) {
        response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(expectedStatus));
    }

    @Then("the response status should be {int} and response code be {int} and content should be {string}")
    public void the_response_should_be(int expectedHttpStatus, int expectedResponseCode, String responseContent) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Parse the JSON response body
        JsonNode jsonResponseNode = mapper.readTree(response.getBody());
        assertThat(response.getStatusCode().value()).isEqualTo(expectedHttpStatus);

        if (response.getStatusCode().is2xxSuccessful()) {
            if (responseContent.startsWith("classpath:")) {
                // Extract the file path from the response content
                String classpathFilePath = responseContent.replace("classpath:", "");
                File jsonFile = ResourceUtils.getFile("classpath:" + classpathFilePath);
                String expectedJsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.toURI())));
                JsonNode expectedJsonNode = mapper.readTree(expectedJsonContent);
                assertThat(expectedJsonNode).isEqualTo(jsonResponseNode);
            } else {
                JsonNode expectedJsonNode = mapper.readTree(responseContent);
                assertThat(expectedJsonNode).isEqualTo(jsonResponseNode);
            }
        } else if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            assertThat(jsonResponseNode.path("code").asInt()).isEqualTo(expectedResponseCode);
            assertThat(jsonResponseNode.path("description").asText()).isEqualTo(responseContent);
        }
    }


    @Then("the response should match the following JSON Path values:")
    public void the_response_should_match_json_path_values(Map<String, String> jsonPathValues) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponseBody = response.getBody();
        ReadContext jsonContext = JsonPath.parse(jsonResponseBody);

        for (Map.Entry<String, String> entry : jsonPathValues.entrySet()) {
            String jsonPath = entry.getKey();
            String expectedValue = entry.getValue();

            // Extract actual value from the JSON response
            Object actualValue = jsonContext.read(jsonPath);
            assertThat(actualValue.toString()).isEqualTo(expectedValue);
        }
    }
}
