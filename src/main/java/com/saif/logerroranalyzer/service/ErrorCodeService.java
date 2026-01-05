package com.saif.logerroranalyzer.service;

import com.saif.logerroranalyzer.dto.ErrorCodeDto;
import com.saif.logerroranalyzer.entity.ErrorCode;
import com.saif.logerroranalyzer.enums.ErrorSeverity;
import com.saif.logerroranalyzer.enums.ErrorType;
import com.saif.logerroranalyzer.repository.ErrorCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class ErrorCodeService {

    @Autowired
    private ErrorCodeRepository errorCodeRepository;

    public List<ErrorCodeDto> getAllActiveErrorCodes() {
        return errorCodeRepository.findByIsActiveTrueOrderBySeverityDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ErrorCodeDto> getErrorCodeById(Long id) {
        return errorCodeRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<ErrorCode> findMatchingErrorCode(String message, String errorCode) {
        // First try exact error code match
        if (errorCode != null) {
            Optional<ErrorCode> exactMatch = errorCodeRepository.findByErrorCodeAndIsActiveTrue(errorCode);
            if (exactMatch.isPresent()) {
                return exactMatch;
            }
        }

        // Try regex pattern matching
        List<ErrorCode> regexCodes = errorCodeRepository.findAllWithRegexPatterns();
        for (ErrorCode code : regexCodes) {
            try {
                Pattern pattern = Pattern.compile(code.getRegexPattern(), Pattern.CASE_INSENSITIVE);
                if (pattern.matcher(message).find()) {
                    return Optional.of(code);
                }
            } catch (Exception e) {
                // Invalid regex pattern, skip
                e.printStackTrace();
                continue;
            }
        }

        // Try keyword matching
        String[] words = message.toLowerCase().split("\\s+");
        for (String word : words) {
            if (word.length() > 3) {
                // Only search for meaningful words
                List<ErrorCode> keywordMatches = errorCodeRepository.findByKeyword(word);
                if (!keywordMatches.isEmpty()) {
                    return Optional.of(keywordMatches.get(0)); // return first match
                }
            }
        }

        return Optional.empty();
    }

    public ErrorCodeDto createErrorCode(ErrorCodeDto dto) {
        ErrorCode entity = convertToEntity(dto);
        ErrorCode saved = errorCodeRepository.save(entity);
        return convertToDto(saved);
    }

    public ErrorCodeDto updateErrorCode(Long id, ErrorCodeDto dto) {
        Optional<ErrorCode> existing = errorCodeRepository.findById(id);
        if (existing.isPresent()) {
            ErrorCode entity = existing.get();
            updateEntityFromDto(entity, dto);
            ErrorCode updated = errorCodeRepository.save(entity);
            return convertToDto(updated);
        }
        return null;
    }

    public boolean deleteErrorCode(Long id) {
        if (errorCodeRepository.existsById(id)) {
            // Soft delete - mark as inactive
            Optional<ErrorCode> errorCode = errorCodeRepository.findById(id);
            if (errorCode.isPresent()) {
                errorCode.get().setIsActive(false);
                errorCodeRepository.save(errorCode.get());
                return true;
            }
        }
        return false;
    }

    public List<ErrorCodeDto> getErrorCodesByType(ErrorType errorType) {
        return errorCodeRepository.findByErrorTypeAndIsActiveTrue(errorType)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ErrorCodeDto> getErrorCodesBySeverity(ErrorSeverity severity) {
        return errorCodeRepository.findBySeverityAndIsActiveTrue(severity)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ErrorCodeDto convertToDto(ErrorCode entity) {
        ErrorCodeDto dto = new ErrorCodeDto();
        dto.setId(entity.getId());
        dto.setErrorCode(entity.getErrorCode());
        dto.setErrorType(entity.getErrorType());
        dto.setDescription(entity.getDescription());
        dto.setSolution(entity.getSolution());
        dto.setSeverity(entity.getSeverity());
        dto.setKeywords(entity.getKeywords());
        dto.setRegexPattern(entity.getRegexPattern());
        dto.setApplicationType(entity.getApplicationType());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private ErrorCode convertToEntity(ErrorCodeDto dto) {
        ErrorCode entity = new ErrorCode();
        entity.setErrorCode(dto.getErrorCode());
        entity.setErrorType(dto.getErrorType());
        entity.setDescription(dto.getDescription());
        entity.setSolution(dto.getSolution());
        entity.setSeverity(dto.getSeverity());
        entity.setKeywords(dto.getKeywords());
        entity.setRegexPattern(dto.getRegexPattern());
        entity.setApplicationType(dto.getApplicationType());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return entity;
    }

    private void updateEntityFromDto(ErrorCode entity, ErrorCodeDto dto) {
        if (dto.getErrorCode() != null)
            entity.setErrorCode(dto.getErrorCode());
        if (dto.getErrorType() != null)
            entity.setErrorType(dto.getErrorType());
        if (dto.getDescription() != null)
            entity.setDescription(dto.getDescription());
        if (dto.getSolution() != null)
            entity.setSolution(dto.getSolution());
        if (dto.getSeverity() != null)
            entity.setSeverity(dto.getSeverity());
        if (dto.getKeywords() != null)
            entity.setKeywords(dto.getKeywords());
        if (dto.getRegexPattern() != null)
            entity.setRegexPattern(dto.getRegexPattern());
        if (dto.getApplicationType() != null)
            entity.setApplicationType(dto.getApplicationType());
        if (dto.getIsActive() != null)
            entity.setIsActive(dto.getIsActive());
    }
}
