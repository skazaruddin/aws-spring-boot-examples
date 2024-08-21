package io.azar.examples.holyquran.service;

import io.azar.examples.holyquran.dto.SurahResponseDto;
import io.azar.examples.holyquran.repository.QuranRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuranServiceImpl implements QuranService {

    @Autowired
    private QuranRepository quranRepository;
    @Override
    public SurahResponseDto findSurahByChapterAndEdition(int chapter, String edition) {
        return quranRepository.findSurahByChapterAndEdition(chapter, edition);
    }
}
