import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  stages: [
    { duration: '1m', target: 500 },   // Ramp up to 10 VUs over 1 minute
    { duration: '2m', target: 1000 },   // Stay at 10 VUs for 2 minutes
    { duration: '2m', target: 1500 },  // Ramp up to 20 VUs over 1 minute
    { duration: '1m', target: 0 },   // Ramp down to 0 VUs over 1 minute
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of requests should complete within 500ms
  },
};

export default function () {

  // Make a GET request to the endpoint
  const response = http.get(`http://localhost:8080/v1/surah/114/en.asad`);

  // Check if the response status code is 200
  if (response.status === 200) {
    console.log(`Successfully retrieved Surah from the Quran.`);
  } else {
    console.error(`Failed to retrieve Surah from the Quran. Status code: ${response.status}`);
  }

  // Sleep for a random duration between 1 to 3 seconds before making the next request
  sleep(Math.random() * 2 + 1);
}
