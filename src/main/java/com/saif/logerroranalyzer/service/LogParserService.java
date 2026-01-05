package com.saif.logerroranalyzer.service;

import com.saif.logerroranalyzer.dto.LogEntryDto;
import com.saif.logerroranalyzer.entity.LogEntry;
import com.saif.logerroranalyzer.enums.LogLevel;
import com.saif.logerroranalyzer.util.DateTimeUtil;
import com.saif.logerroranalyzer.util.LogPatternMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogParserService {

    @Autowired
    private LogPatternMatcher patternMatcher;

    @Autowired
    private DateTimeUtil dateTimeUtil;

    public List<LogEntryDto> parseLogContent(String logContent, String fileName){
        List<LogEntryDto> logEntries = new ArrayList<>();

        if(logContent == null || logContent.trim().isEmpty()){
            return logEntries;
        }

        String[] lines = logContent.split("\\r?\\n");

        for(String line: lines){
            if(line.trim().isEmpty() || isIgnoredLine(line)){
                continue;
            }

            LogEntryDto entry = parseLogLine(line,fileName);
            if(entry != null ){
                logEntries.add(entry);
            }
        }
        return logEntries;

    }

    private  boolean isIgnoredLine(String line){
        //Skip lines that are not actual log entries
        return line.startsWith("//") || line.startsWith("#") ||
                line.trim().equals("---") || line.matches("^\\s*$");
    }

    private LogEntryDto parseLogLine(String line, String fileName){
        try{
            LogLevel logLevel = patternMatcher.extractLogLevel(line);
            String component = patternMatcher.extractComponent(line);
            String message = patternMatcher.extractMessage(line);
            String errorCode = patternMatcher.extractErrorCode(line);
            String timestampStr = patternMatcher.extractTimestampString(line);
            LocalDateTime timestamp = dateTimeUtil.parseTimestamp(timestampStr);

            LogEntryDto entry = new LogEntryDto();
            entry.setTimestamp(timestamp);
            entry.setLogLevel(logLevel);
            entry.setComponent(component);
            entry.setMessage(message);
            entry.setErrorCode(errorCode);
            entry.setFullLine(line);
            entry.setFileName(fileName);

            return entry;
        }catch (Exception e){
            // Log parsing error, but don't fail the entire process
            System.err.println("Error parsing line: "+line+" - "+e.getMessage());
            return null;
        }
    }

    public LogEntry convertToEntity(LogEntryDto dto, String sessionId){
        LogEntry entity = new LogEntry();
        entity.setTimestamp(dto.getTimestamp());
        entity.setLogLevel(dto.getLogLevel());
        entity.setComponent(dto.getComponent());
        entity.setMessage(dto.getMessage());
        entity.setFullLine(dto.getFullLine());
        entity.setErrorCode(dto.getErrorCode());
        entity.setFileName(dto.getFileName());
        entity.setAnalysisSession(sessionId);
        return entity;
    }
}
