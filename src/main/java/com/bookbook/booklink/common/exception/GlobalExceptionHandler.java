package com.bookbook.booklink.common.exception;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    // @Valid 유효성 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        String path = request.getRequestURI();
        String method = request.getMethod();
        String query = request.getQueryString(); // null일 수 있음

        // FieldError 목록에서 상세 메시지 조합
        String detailMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        if (query != null) {
            log.error("Validation failed [{} {}?{}]: {}", method, path, query, detailMessage);
        } else {
            log.error("Validation failed [{} {}]: {}", method, path, detailMessage);
        }

        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.error(errorCode, path));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse<String>> handleCustomException(CustomException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        String path = request.getRequestURI();
        String method = request.getMethod();
        String query = request.getQueryString();
        String message = ex.getMessage();

        // 어떤 비즈니스 로직에서 발생했는지 로그에 남김
        if (query != null) {
            log.error("Business exception [{} {}?{}]: code={}, message={}", method, path, query, errorCode.name(), message);
        } else {
            log.error("Business exception [{} {}]: code={}, message={}", method, path, errorCode.name(), message);
        }

        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.error(errorCode, path));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<String>> handleAuthenticationException(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String query = request.getQueryString();
        String message = ex.getMessage();

        ErrorCode errorCode = ErrorCode.USER_LOGIN_FAILED;

        if (query != null) {
            log.error("Authentication exception [{} {}?{}]: code={}, message={}", method, path, query, errorCode.name(), message);
        } else {
            log.error("Authentication exception [{} {}]: code={}, message={}", method, path, errorCode.name(), message);
        }


        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.error(errorCode, path));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<BaseResponse<String>> handleAuthorizationDeniedException(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String query = request.getQueryString();
        String message = ex.getMessage();

        ErrorCode errorCode = ErrorCode.METHOD_UNAUTHORIZED;

        if (query != null) {
            log.error("AuthorizationDenied exception [{} {}?{}]: code={}, message={}", method, path, query, errorCode.name(), message);
        } else {
            log.error("AuthorizationDenied exception [{} {}]: code={}, message={}", method, path, errorCode.name(), message);
        }

        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.error(errorCode, path));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleException(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String query = request.getQueryString();

        if (query != null) {
            log.error("Unhandled exception [{} {}?{}]: {}", method, path, query, ex.getMessage(), ex);
        } else {
            log.error("Unhandled exception [{} {}]: {}", method, path, ex.getMessage(), ex);
        }

        ErrorCode code = ErrorCode.UNKNOWN_ERROR;

        return ResponseEntity
                .status(code.getHttpStatus().value())
                .body(BaseResponse.error(code, path));
    }

}
