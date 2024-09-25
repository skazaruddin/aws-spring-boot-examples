package io.azar.examples.holyquran.service;

import io.azar.examples.holyquran.dto.SurahResponseDto;

import org.springframework.http.ResponseEntity;

public interface QuranService {
    SurahResponseDto findSurahByChapterAndEdition(int chapter, String edition);
}
