package com.saif.logerroranalyzer.controller;

import com.saif.logerroranalyzer.dto.ErrorAnalysisResult;
import com.saif.logerroranalyzer.dto.LogAnalysisRequest;
import com.saif.logerroranalyzer.service.ErrorAnalysisService;
import com.saif.logerroranalyzer.service.ReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "*")
public class LogAnalysisController {

    @Autowired
    private ErrorAnalysisService errorAnalysisService;

    @Autowired
    private ReportService reportService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ErrorAnalysisResult>> analyzeUploadedFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "logType", defaultValue = "GENERIC") String logType) {
        try {
            String logContent = new String(file.getBytes());
            String fileName = file.getOriginalFilename();

            List<ErrorAnalysisResult> results = errorAnalysisService.analyzeLogContent(logContent, fileName, logType);

            return ResponseEntity.ok(results);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/text", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ErrorAnalysisResult>> analyzeTextContent(@RequestBody LogAnalysisRequest request) {
        List<ErrorAnalysisResult> results = errorAnalysisService.analyzeLogContent(
                request.getLogContent(), request.getFileName());
        return ResponseEntity.ok(results);
    }

    @PostMapping("/export/csv")
    public ResponseEntity<byte[]> exportCSV(@RequestBody List<ErrorAnalysisResult> results) {
        try {
            byte[] csvData = reportService.generateCSVReport(results);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "error_analysis_report_" + timestamp + ".csv";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(csvData.length);

            return new ResponseEntity<>(csvData, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/export/html")
    public ResponseEntity<String> exportHTML(@RequestBody List<ErrorAnalysisResult> results) {
        String htmlReport = reportService.generateHTMLReport(results);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return new ResponseEntity<>(htmlReport, headers, HttpStatus.OK);
    }
}
