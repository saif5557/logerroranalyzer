package com.saif.logerroranalyzer.service;

import com.saif.logerroranalyzer.dto.ErrorAnalysisResult;
import com.saif.logerroranalyzer.dto.LogEntryDto;
import com.saif.logerroranalyzer.entity.ErrorCode;
import com.saif.logerroranalyzer.enums.ErrorSeverity;
import com.saif.logerroranalyzer.enums.ErrorType;
import com.saif.logerroranalyzer.enums.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ErrorAnalysisService {

    @Autowired
    private LogParserService logParserService;

    @Autowired
    private ErrorCodeService errorCodeService;

    public List<ErrorAnalysisResult> analyzeLogContent(String logContent, String fileName) {
        return analyzeLogContent(logContent, fileName, "GENERIC");
    }

    public List<ErrorAnalysisResult> analyzeLogContent(String logContent, String fileName, String logType) {
        java.util.Map<String, ErrorAnalysisResult> aggregatedResults = new java.util.HashMap<>();

        // Parse log content
        List<LogEntryDto> logEntries = logParserService.parseLogContent(logContent, fileName);

        // Analyze each log entry
        // Analyze each log entry
        for (LogEntryDto entry : logEntries) {
            if (shouldAnalyze(entry)) {
                ErrorAnalysisResult result = analyzeLogEntry(entry, logType);

                String key = generateGroupingKey(result);

                if (aggregatedResults.containsKey(key)) {
                    aggregatedResults.get(key).incrementCount();
                } else {
                    aggregatedResults.put(key, result);
                }
            }
        }
        return new ArrayList<>(aggregatedResults.values());
    }

    private String generateGroupingKey(ErrorAnalysisResult result) {
        if (result.isMatched() && result.getErrorCode() != null && !result.getErrorCode().isEmpty()) {
            return result.getErrorCode();
        }
        // For unmatched or unknown errors, group by message content
        // You might want to strip timestamps or variable parts if possible,
        // but for now, exact message matching is the safest default.
        return result.getMessage();
    }

    private ErrorAnalysisResult analyzeLogEntry(LogEntryDto entry, String logType) {
        // Try find matching error code
        Optional<ErrorCode> matchedErrorCode = errorCodeService.findMatchingErrorCode(
                entry.getMessage(), entry.getErrorCode());

        ErrorAnalysisResult result = new ErrorAnalysisResult();
        result.setTimestamp(entry.getTimestamp());
        result.setLogLevel(entry.getLogLevel());
        result.setComponent(entry.getComponent());
        result.setMessage(entry.getMessage());
        result.setFullLogLine(entry.getFullLine());
        result.setFileName(entry.getFileName());

        if (matchedErrorCode.isPresent()) {
            ErrorCode errorCode = matchedErrorCode.get();
            result.setErrorCode(errorCode.getErrorCode());
            result.setErrorType(errorCode.getErrorType());
            result.setDescription(errorCode.getDescription());
            result.setSolution(errorCode.getSolution());
            result.setSeverity(errorCode.getSeverity());
            result.setMatched(true);
        } else {
            // Create default analysis for unmatched errors
            result.setErrorCode(entry.getErrorCode() != null ? entry.getErrorCode() : "UNKNOWN");
            result.setErrorType(determineErrorType(entry, logType));
            result.setDescription("Unmatched error: " + entry.getMessage());
            result.setDescription("Unmatched error: " + entry.getMessage());
            result.setSolution(generateDefaultSolution(entry, logType));
            result.setSeverity(determineServerityFromLogLevel(entry.getLogLevel()));
            result.setSeverity(determineServerityFromLogLevel(entry.getLogLevel()));
            result.setMatched(false);
        }
        return result;
    }

    private ErrorSeverity determineServerityFromLogLevel(LogLevel logLevel) {
        switch (logLevel) {
            case FATAL:
                return ErrorSeverity.CRITICAL;
            case CRITICAL:
                return ErrorSeverity.HIGH;
            case ERROR:
                return ErrorSeverity.HIGH;
            case WARNING:
                return ErrorSeverity.MEDIUM;
            default:
                return ErrorSeverity.LOW;
        }
    }

    private String generateDefaultSolution(LogEntryDto entry, String logType) {
        ErrorType errorType = determineErrorType(entry, logType);

        switch (errorType) {
            case MES_APPLICATION_ERROR:
                return "Check MES application logs, verify configuration, and restart MES services if necessary.";
            case NETWORK_ERROR:
                return "Check network connectivity, verify server status, and review firewall settings";
            case HARDWARE_ERROR:
                return "Verify device connections, check cables, and restart hardware components.";
            case DATABASE_ERROR:
                return "Check database connectivity, verify credentials, and review query syntax.";
            case CONFIGURATION_ERROR:
                return "Review configuration files, verify parameter values, and check syntax.";
            case AUTHENTICATION_ERROR:
                return "Verify credentials, check authentication settings, and review permissions.";
            case TIMEOUT_ERROR:
                return "Increase timeout values, check system performance, and verify network stability.";
            case PERMISSION_ERROR:
                return "Check user permissions, verify access rights, and review security settings.";
            default:
                return "Review system logs, check system resources, and contact technical support if needed.";

        }
    }

    private ErrorType determineErrorType(LogEntryDto entry, String logType) {
        String message = entry.getMessage().toLowerCase();
        String component = entry.getComponent().toLowerCase();

        if ("MES".equalsIgnoreCase(logType)) {
            return ErrorType.MES_APPLICATION_ERROR;
        }

        if (message.contains("network") || message.contains("connection") ||
                message.contains("socket") || message.contains("tcp")) {
            return ErrorType.NETWORK_ERROR;
        }

        if (message.contains("database") || message.contains("sql") ||
                message.contains("query")) {
            return ErrorType.DATABASE_ERROR;
        }

        if (component.contains("serial") || component.contains("usb") ||
                component.contains("device") || component.contains("hardware")) {
            return ErrorType.HARDWARE_ERROR;
        }

        if (message.contains("config") || message.contains("setting") ||
                message.contains("parameter")) {
            return ErrorType.CONFIGURATION_ERROR;
        }
        if (message.contains("auth") || message.contains("login") ||
                message.contains("credential")) {
            return ErrorType.AUTHENTICATION_ERROR;
        }
        if (message.contains("timeout") || message.contains("expired")) {
            return ErrorType.TIMEOUT_ERROR;
        }

        if (message.contains("permission") || message.contains("access denied")) {
            return ErrorType.PERMISSION_ERROR;
        }

        return ErrorType.SYSTEM_ERROR;
    }

    private boolean shouldAnalyze(LogEntryDto entry) {
        // Analyze entries that are errors, warnings, critical, or fatal
        LogLevel level = entry.getLogLevel();
        return level == LogLevel.ERROR ||
                level == LogLevel.WARNING ||
                level == LogLevel.CRITICAL ||
                level == LogLevel.FATAL ||
                entry.getErrorCode() != null ||
                containsErrorKeywords(entry.getMessage());
    }

    private boolean containsErrorKeywords(String message) {
        if (message == null)
            return false;

        String lowerMessage = message.toLowerCase();
        return lowerMessage.contains("error") ||
                lowerMessage.contains("fail") ||
                lowerMessage.contains("exception") ||
                lowerMessage.contains("timeout") ||
                lowerMessage.contains("refused") ||
                lowerMessage.contains("denied") ||
                lowerMessage.contains("missing") ||
                lowerMessage.contains("not found") ||
                lowerMessage.contains("invalid") ||
                lowerMessage.contains("unable") ||
                lowerMessage.contains("cannot");
    }
}
