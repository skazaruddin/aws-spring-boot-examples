Feature: Surah API Testing

  @RunMe
  @isolated

  # Scenario for Success Responses
  Scenario: Success: Retrieve surah 114 information successfully
    Given a chapter number of 114 and an edition identifier of "en.asad"
    When the client sends a GET request to "/v1/surah/114/en.asad"
    Then the response status should be 200
    And the response should match the following JSON Path values:
      | $.number                 | 114                                            |
      | $.name                   | سُورَةُ النَّاسِ                               |
      | $.englishName            | An-Naas                                        |
      | $.revelationType         | Meccan                                         |
      | $.numberOfAyahs          | 6                                              |
      | $.ayahs[0].number        | 6231                                           |
      | $.ayahs[0].text          | SAY: "I seek refuge with the Sustainer of men, |
      | $.ayahs[0].juz           | 30                                             |
      | $.ayahs[0].manzil        | 7                                              |
      | $.ayahs[0].page          | 604                                            |
      | $.ayahs[0].ruku          | 556                                            |
      | $.ayahs[0].sajda         | false                                          |
      | $.ayahs[0].numberInSurah | 1                                              |
      | $.ayahs[0].hizbQuarter   | 240                                            |
      | $.edition.identifier     | en.asad                                        |
      | $.edition.language       | en                                             |
      | $.edition.name           | Asad                                           |
      | $.edition.format         | text                                           |
      | $.edition.type           | translation                                    |
      | $.edition.direction      | ltr                                            |
      | $.edition.englishName    | Muhammad Asad                                  |
      | $.englishNameTranslation | Mankind                                        |

  @RunMe
  @isolated
  Scenario Outline: Failure: When surah number not between 1 and 114
    Given a chapter number of <chapter_number> and an edition identifier of "<edition_identifier>"
    When the client sends a GET request to "<url>"
    Then the response status should be <response_http_status> and response code be <response_code> and content should be "<response_content>"

    Examples:
      | chapter_number | edition_identifier | url                    | response_http_status | response_code | response_content                          |
      | 1141           | en.asad            | /v1/surah/1141/en.asad | 400                  | 404           | Surat number should be between 1 and 114. |

  @RunMe
  @isolated
  Scenario Outline: Failure: When the backend quran api service is down or not available
    Given a chapter number of <chapter_number> and an edition identifier of "<edition_identifier>"
    When the client sends a GET request to "<url>"
    Then the response status should be <response_http_status> and response code be <response_code> and content should be "<response_content>"

    Examples:
      | chapter_number | edition_identifier | url                    | response_http_status | response_code | response_content                        |
      | 1150           | en.asad            | /v1/surah/1150/en.asad | 500                  | 503           | The service is temporarily unavailable. |
