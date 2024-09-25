package io.azar.examples.holyquran.repository;

import io.azar.examples.holyquran.client.QuranApiClient;
import io.azar.examples.holyquran.dto.SurahResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuranRepositoryImpl implements QuranRepository {

    private final QuranApiClient quranApiClient;

    @Autowired
    public QuranRepositoryImpl(QuranApiClient quranApiClient) {
        this.quranApiClient = quranApiClient;
    }

    @Override
    public SurahResponseDto findSurahByChapterAndEdition(Integer surah, String edition) {
        return quranApiClient.findSurahByChapterAndEdition(surah, edition);
    }
}
