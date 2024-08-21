package io.azar.examples.holyquran.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurahResponseDto {

    private int code;
    private String status;
    private Data data;

    // Constructors, getters, and setters
    @lombok.Data
    public static class Data {
        private int number;
        private String name;
        @JsonProperty("englishName")
        private String englishName;
        @JsonProperty("englishNameTranslation")
        private String englishNameTranslation;
        @JsonProperty("revelationType")
        private String revelationType;
        @JsonProperty("numberOfAyahs")
        private int numberOfAyahs;
        private List<Ayah> ayahs;
        private Edition edition;

        // Constructors, getters, and setters
    }

    @lombok.Data
    public static class Ayah {
        private int number;
        private String text;
        @JsonProperty("numberInSurah")
        private int numberInSurah;
        private int juz;
        private int manzil;
        private int page;
        private int ruku;
        @JsonProperty("hizbQuarter")
        private int hizbQuarter;
        private boolean sajda;

        // Constructors, getters, and setters
    }

    @lombok.Data
    public static class Edition {
        private String identifier;
        private String language;
        private String name;
        @JsonProperty("englishName")
        private String englishName;
        private String format;
        private String type;
        private String direction;

        // Constructors, getters, and setters
    }
}

