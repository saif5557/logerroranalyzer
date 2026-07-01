package com.saif.logerroranalyzer.controller;

import com.saif.logerroranalyzer.dto.ErrorCodeDto;
import com.saif.logerroranalyzer.enums.ApplicationType;
import com.saif.logerroranalyzer.enums.ErrorSeverity;
import com.saif.logerroranalyzer.enums.ErrorType;
import com.saif.logerroranalyzer.service.ErrorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/error-codes")
@CrossOrigin(origins = "*")
public class ErrorCodeController {

    @Autowired
    private ErrorCodeService errorCodeService;

    @GetMapping
    public ResponseEntity<List<ErrorCodeDto>> getAllErrorCodes() {
        List<ErrorCodeDto> errorCodes = errorCodeService.getAllActiveErrorCodes();
        return ResponseEntity.ok(errorCodes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ErrorCodeDto> getErrorCodeById(@PathVariable Long id) {
        Optional<ErrorCodeDto> errorCodes = errorCodeService.getErrorCodeById(id);
        return errorCodes.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ErrorCodeDto> createErrorCode(@RequestBody ErrorCodeDto errorCodeDto) {
        try {
            ErrorCodeDto created = errorCodeService.createErrorCode(errorCodeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ErrorCodeDto> updateErrorCode(@PathVariable Long id, @RequestBody ErrorCodeDto errorCodeDto) {
        ErrorCodeDto updated = errorCodeService.updateErrorCode(id, errorCodeDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteErrorCode(@PathVariable Long id) {
        boolean deleted = errorCodeService.deleteErrorCode(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-type/{errorType}")
    public ResponseEntity<List<ErrorCodeDto>> getErrorCodesByType(@PathVariable ErrorType errorType) {
        List<ErrorCodeDto> errorCodes = errorCodeService.getErrorCodesByType(errorType);
        return ResponseEntity.ok(errorCodes);
    }

    @GetMapping("/by-severity/{severity}")
    public ResponseEntity<List<ErrorCodeDto>> getErrorCodesBySeverity(@PathVariable ErrorSeverity errorSeverity) {
        List<ErrorCodeDto> errorCodes = errorCodeService.getErrorCodesBySeverity(errorSeverity);
        return ResponseEntity.ok(errorCodes);
    }

    @GetMapping("/types")
    public ResponseEntity<ErrorType[]> getErrorTypes() {
        return ResponseEntity.ok(ErrorType.values());
    }

    @GetMapping("/severities")
    public ResponseEntity<ErrorSeverity[]> getSeverities() {
        return ResponseEntity.ok(ErrorSeverity.values());
    }

    @GetMapping("/applications")
    public ResponseEntity<ApplicationType[]> getApplicationTypes() {
        return ResponseEntity.ok(ApplicationType.values());
    }
}
