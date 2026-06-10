package com.urlanalyzer.backend;

import com.urlanalyzer.backend.model.AnalysisResponse;
import com.urlanalyzer.backend.service.UrlAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")


public class AnalysisController {
    @Autowired
    private UrlAnalyzerService service;
    @PostMapping("/analyze")
    public AnalysisResponse analyze(
            @RequestBody Map<String, String> request) {

        return service.analyze(request.get("url"));
    }
}
