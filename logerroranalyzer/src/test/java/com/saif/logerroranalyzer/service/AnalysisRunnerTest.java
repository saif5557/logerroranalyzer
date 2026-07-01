package com.saif.logerroranalyzer.service;

import com.saif.logerroranalyzer.dto.ErrorAnalysisResult;
import com.saif.logerroranalyzer.repository.ErrorCodeRepository;
import com.saif.logerroranalyzer.util.DateTimeUtil;
import com.saif.logerroranalyzer.util.LogPatternMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AnalysisRunnerTest {

    @Mock
    private ErrorCodeRepository errorCodeRepository;

    @InjectMocks
    private ErrorCodeService errorCodeService;

    @Spy
    private LogPatternMatcher logPatternMatcher = new LogPatternMatcher();

    @Spy
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();

    @InjectMocks
    private LogParserService logParserService;

    @InjectMocks
    private ErrorAnalysisService errorAnalysisService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(logParserService, "patternMatcher", logPatternMatcher);
        ReflectionTestUtils.setField(logParserService, "dateTimeUtil", dateTimeUtil);

        ReflectionTestUtils.setField(errorAnalysisService, "logParserService", logParserService);
        ReflectionTestUtils.setField(errorAnalysisService, "errorCodeService", errorCodeService);
    }

    @Test
    void runAnalysisOnProvidedLog() {
        String logContent = "2026-01-19 19:00:01,868 [1] INFO  Core.Helpers.CommandInvoker - Execute command : C:\\Program Files (x86)\\Renesas Electronics\\Programming Tools\\Renesas Flash Programmer V3.11/rfp-cli.exe --lt -device rl78\n"
                +
                "2026-01-19 19:00:02,142 [1] INFO  Core.Helpers.CommandInvoker - Completed in : 270ms, Process exit code : 0, Start Time: 19-01-2026 07:00:01 PM, TotalProcessor Time : 00:00:00.2500000, UserProcessorTime : 00:00:00.2343750\n"
                +
                "2026-01-19 19:01:49,297 [1] INFO  Core.CommandBuilder.TempSensorCommandGroup - command initialized for com port COM35 is TemperatureSensorCommand\n"
                +
                "2026-01-19 19:01:49,301 [1] INFO  ZigLibrary.Helper.V2.SerialPortService - Com opening request COM35, IsOpen False\n"
                +
                "2026-01-19 19:01:49,824 [1] INFO  ZigLibrary.Helper.V2.SerialPortService - Com opened COM35, IsOpen True\n"
                +
                "2026-01-19 19:01:49,828 [1] ERROR ZigLibrary.Manager.TempSensorBufferManager - Partial Data received at Temp Sensor Com port.\n"
                +
                "2026-01-19 19:01:50,730 [1] INFO  ZigLibrary.Helper.V2.SerialPortHelper - Port Number COM35, TemperatureSensorCommand Command Timed out\n"
                +
                "2026-01-19 19:01:50,963 [1] INFO  ZigLibrary.Helper.V2.SerialPortService - Closing Com COM35, IsOpen True";

        System.out.println("Running Analysis on Log Content...");
        List<ErrorAnalysisResult> results = errorAnalysisService.analyzeLogContent(logContent, "test_log.txt",
                "GENERIC");

        System.out.println("Analysis Results Found: " + results.size());
        for (ErrorAnalysisResult result : results) {
            System.out.println("--------------------------------------------------");
            System.out.println("Level: " + result.getLogLevel());
            System.out.println("Component: " + result.getComponent());
            System.out.println("Message: " + result.getMessage());
            System.out.println("Error Type: " + result.getErrorType());
            System.out.println("Solution: " + result.getSolution());
        }
        System.out.println("--------------------------------------------------");
    }
}
