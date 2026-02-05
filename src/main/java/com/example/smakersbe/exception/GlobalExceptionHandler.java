package com.example.smakersbe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Revision 충돌 (Optimistic Lock)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException e) {

        if ("Revision conflict".equals(e.getMessage())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", 409,
                            "error", "CONFLICT",
                            "message", "Revision conflict. Please reload workflow and try again.",
                            "timestamp", LocalDateTime.now()
                    ));
        }

        // 그 외 IllegalStateException
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "error", "BAD_REQUEST",
                        "message", e.getMessage(),
                        "timestamp", LocalDateTime.now()
                ));
    }

    /**
     * 잘못된 요청 (예: uuid 없음, node 참조 오류 등)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "error", "BAD_REQUEST",
                        "message", e.getMessage(),
                        "timestamp", LocalDateTime.now()
                ));
    }
}
