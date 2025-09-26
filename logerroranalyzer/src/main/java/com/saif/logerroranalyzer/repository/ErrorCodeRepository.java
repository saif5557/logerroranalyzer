package com.saif.logerroranalyzer.repository;

import com.saif.logerroranalyzer.entity.ErrorCode;
import com.saif.logerroranalyzer.enums.ErrorSeverity;
import com.saif.logerroranalyzer.enums.ErrorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ErrorCodeRepository extends JpaRepository<ErrorCode, Long> {

    Optional<ErrorCode> findByErrorCodeAndIsActiveTrue(String errorCode);

    List<ErrorCode> findByIsActiveTrueOrderBySeverityDesc();

    List<ErrorCode> findByErrorTypeAndIsActiveTrue(ErrorType errorType);

    List<ErrorCode> findBySeverityAndIsActiveTrue(ErrorSeverity severity);

    @Query("SELECT e FROM ErrorCode e WHERE e.isActive = true AND "
    +"(LOWER(e.keywords) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            +"LOWER(e.description) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "
            +"LOWER(e.errorCode) LIKE LOWER(CONCAT('%',:keyword,'%')))")
    List<ErrorCode> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT e FROM ErrorCode e WHERE e.isActive = true AND e.regexPattern IS NOT NULL")
    List<ErrorCode> findAllWithRegexPatterns();

    @Query("SELECT COUNT(e) FROM ErrorCode e WHERE e.isActive = true")
    long countActiveErrorCodes();


}
