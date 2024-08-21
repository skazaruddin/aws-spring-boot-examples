package io.azar.examples.holyquran.controller;

import io.azar.examples.holyquran.dto.SurahResponseDto;
import io.azar.examples.holyquran.service.QuranService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/surah")
public class QuranController {
    private QuranService quranService;

    public QuranController(QuranService quranService) {
        this.quranService = quranService;
    }

    /**
     * Retrieve information about a specific chapter (Surah) from the Quran based on the provided chapter number
     * and the edition of the translation.
         */
    @GetMapping("/{chapter}/{edition}")
    public ResponseEntity<SurahResponseDto> getSurah(
            @PathVariable(name = "chapter") Integer chapter,
            @PathVariable(name = "edition") String edition) {
        return ResponseEntity.ok().body(quranService.findSurahByChapterAndEdition(chapter, edition));
    }

}
