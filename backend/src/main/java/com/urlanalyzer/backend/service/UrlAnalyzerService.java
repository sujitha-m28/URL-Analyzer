package com.urlanalyzer.backend.service;

import com.urlanalyzer.backend.model.AnalysisResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class UrlAnalyzerService {

    private final WebClient webClient;

    public UrlAnalyzerService(WebClient webClient) {
        this.webClient = webClient;
    }

    public AnalysisResponse analyze(String url) {

        // Auto-fix URL
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        AnalysisResponse response = new AnalysisResponse();
        List<String> defects = new ArrayList<>();

        long start = System.currentTimeMillis();

        try {
            String finalUrl = url;

            String result = webClient
                    .get()
                    .uri(finalUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            long timeTaken = System.currentTimeMillis() - start;

            response.setStatusCode(200);
            response.setTimeTaken(timeTaken);
            response.setSuccess(true);

            if (timeTaken > 3000) {
                defects.add("Slow Response");
            }

        } catch (Exception e) {

            long timeTaken = System.currentTimeMillis() - start;

            response.setStatusCode(500);
            response.setTimeTaken(timeTaken);
            response.setSuccess(false);

            defects.add("URL Unreachable / Blocked / Timeout");
        }

        response.setDefects(defects);

        return response;
    }
}