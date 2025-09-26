package com.saif.logerroranalyzer.service;

import com.saif.logerroranalyzer.dto.ErrorAnalysisResult;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public byte[] generateCSVReport(List<ErrorAnalysisResult> results)throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // Write CSV header
        writer.println("Timestamp,Error Code,Error Type,Log Level,Component,Description,Solution,Severity,Matched,Message");

        // Write data rows
        for(ErrorAnalysisResult result: results){
            writer.printf(
                    "%s,%s,%s,%s,%s,\"%s\",\"%s\",%s,%s,\"%s\"%n",
                    result.getTimestamp().format(DATETIME_FORMATTER),
                    escapeCSV(result.getErrorCode()),
                    escapeCSV(result.getErrorType().getDisplayName()),
                    escapeCSV(result.getLogLevel().getDisplayName()),
                    escapeCSV(result.getComponent()),
                    escapeCSV(result.getDescription()),
                    escapeCSV(result.getSolution()),
                    escapeCSV(result.getSeverity().getDisplayName()),
                    result.isMatched(),
                    escapeCSV(result.getMessage())
            );
        }

        writer.flush();
        writer.close();

        return outputStream.toByteArray();
    }

    private String escapeCSV(String value) {
        if(value == null) return "";
        return value.replace("\"", "\"\"");
    }

    public String generateHTMLReport(List<ErrorAnalysisResult> results){
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><title>Error Analysis Report</title>");
        html.append("<style>");
        html.append("table { border-collapse: collapse; width: 100%; }");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append(".severity-critical { background-color: #ffebee; }");
        html.append(".severity-high { background-color: #fff3e0; }");
        html.append(".severity-medium { background-color: #fffde7; }");
        html.append(".severity-low { background-color: #e8f5e8; }");
        html.append("</style>");
        html.append("</head><body>");
        html.append("<h1>Error Analysis Report</h1>");
        html.append("<table>");
        html.append("<tr><th>Timestamp</th><th>Error Code</th><th>Type</th><th>Level</th>");
        html.append("<th>Component</th><th>Description</th><th>Solution</th><th>Severity</th></tr>");



        for (ErrorAnalysisResult result : results) {
            String rowClass = "severity-" + result.getSeverity().name().toLowerCase();
            html.append(String.format("<tr class=\"%s\">", rowClass));
            html.append(String.format("<td>%s</td>", result.getTimestamp().format(DATETIME_FORMATTER)));
            html.append(String.format("<td>%s</td>", result.getErrorCode()));
            html.append(String.format("<td>%s</td>", result.getErrorType().getDisplayName()));
            html.append(String.format("<td>%s</td>", result.getLogLevel().getDisplayName()));
            html.append(String.format("<td>%s</td>", result.getComponent()));
            html.append(String.format("<td>%s</td>", escapeHtml(result.getDescription())));
            html.append(String.format("<td>%s</td>", escapeHtml(result.getSolution())));
            html.append(String.format("<td>%s</td>", result.getSeverity().getDisplayName()));
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("</body></html>");

        return html.toString();
    }

    private String escapeHtml(String value){
        if(value == null) return "";
        return value.replace("&","&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;")
                .replace("\"","&quot;")
                .replace("'","&#39;");
    }
}
