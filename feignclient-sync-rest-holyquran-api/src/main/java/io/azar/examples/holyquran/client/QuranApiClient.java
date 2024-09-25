package io.azar.examples.holyquran.client;

import io.azar.examples.holyquran.config.QuranApiClientConfiguration;
import io.azar.examples.holyquran.config.QuranApiErrorDecoder;
import io.azar.examples.holyquran.dto.SurahResponseDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "quran-api", url = "${api.quranapi.host}", configuration = {
    QuranApiClientConfiguration.class, QuranApiErrorDecoder.class})
public interface QuranApiClient {

    @GetMapping("/v1/surah/{surah}/{edition}")
    SurahResponseDto findSurahByChapterAndEdition(@PathVariable("surah") Integer surah,
                                                  @PathVariable("edition") String edition);
}
