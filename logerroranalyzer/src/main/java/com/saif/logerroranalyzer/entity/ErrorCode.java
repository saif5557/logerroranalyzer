package com.saif.logerroranalyzer.entity;

import com.saif.logerroranalyzer.enums.ErrorSeverity;
import com.saif.logerroranalyzer.enums.ErrorType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "error_codes")
public class ErrorCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "error_code", unique = true, nullable = false)
    private String errorCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "error_type")
    private ErrorType errorType;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "solution", length = 2000)
    private String solution;

    @Enumerated(EnumType.STRING)
    @Column(name = "serverity")
    private ErrorSeverity severity;

    @Column(name = "keywords")
    private String keywords; // Comma-separated keywords for matching

    @Column(name = "regex_pattern")
    private String regexPattern; // Regex pattern for advaced matching

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column( name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Constructors
    public ErrorCode(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ErrorCode(String errorCode, ErrorType errorType, String description, String solution, ErrorSeverity severity, String keywords) {
        this();
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.description = description;
        this.solution = solution;
        this.severity = severity;
        this.keywords = keywords;
    }

    // Getter and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public ErrorSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(ErrorSeverity severity) {
        this.severity = severity;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getISActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getIsActive() {
        return isActive;
    }
}
