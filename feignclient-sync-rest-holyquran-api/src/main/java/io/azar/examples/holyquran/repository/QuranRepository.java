package io.azar.examples.holyquran.repository;


import io.azar.examples.holyquran.dto.SurahResponseDto;

public interface QuranRepository {
    SurahResponseDto findSurahByChapterAndEdition(Integer surah, String edition);

}
