package io.azar.examples.holyquran;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
    "api.quranapi.host=http://localhost:${wiremock.server.port}"
})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"unit-test"})
public class SyncHolyQuranApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldRetrieveSurahInformationSuccessfullyTest() throws Exception {
        int chapter = 114;
        String edition = "en.asad";

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/surah/{chapter}/{edition}", chapter, edition)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(114))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("سُورَةُ النَّاسِ"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs.length()").value(6))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs[0].number").value(6231))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs[0].text")
                .value("SAY: \"I seek refuge with the Sustainer of men,"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs[0].juz").value(30))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs[0].manzil").value(7))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs[0].page").value(604))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs[0].ruku").value(556))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs[0].sajda").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs[0].numberInSurah").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ayahs[0].hizbQuarter").value(240))
            .andExpect(MockMvcResultMatchers.jsonPath("$.edition.identifier").value("en.asad"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.edition.language").value("en"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.edition.name").value("Asad"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.edition.format").value("text"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.edition.type").value("translation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.edition.direction").value("ltr"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.edition.englishName").value("Muhammad Asad"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.englishName").value("An-Naas"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.englishNameTranslation").value("Mankind"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.revelationType").value("Meccan"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfAyahs").value(6));
    }

    /**
     * Tests the behavior of the Surah endpoint when providing a bad request.
     * <p>
     * This test case performs an HTTP GET request to the Surah endpoint with an invalid chapter number.
     * It expects the endpoint to return a 400 Bad Request status along with an error message indicating that
     * the requested chapter does not exist in the target system.
     */
    @Test
    public void shouldReturnBadRequestForInvalidChapterNumberTest() throws Exception {
        int chapter = 1141;
        String edition = "en.asad";

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/surah/{chapter}/{edition}", chapter, edition)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                .value("Surat number should be between 1 and 114."));
    }


    /**
     * Tests the behavior of the Surah endpoint when the service is temporarily unavailable.
     * <p>
     * This test case performs an HTTP GET request to the Surah endpoint with a chapter number while service doesn't have capacity to handle more requests.
     * It expects the endpoint to return a 5xx Server Error status along with an error message indicating that
     * the service is temporarily unavailable.
     */
    @Test
    public void shouldReturnServiceUnavailableTest() throws Exception {
        int chapter = 1150;
        String edition = "en.asad";

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/surah/{chapter}/{edition}", chapter, edition)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(503))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                .value("The service is temporarily unavailable."));
    }
}
