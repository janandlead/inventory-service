package com.javacodeex.inventory.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Converts known application exceptions into consistent HTTP error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles requests for products that do not exist.
     *
     * @param exception exception containing the missing product message
     * @param request current HTTP request
     * @return a 404 response with a structured error body
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(
            ProductNotFoundException exception, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", exception.getMessage(), request);
    }

    /**
     * Handles invalid request arguments such as non-positive quantities.
     *
     * @param exception exception containing the validation message
     * @param request current HTTP request
     * @return a 400 response with a structured error body
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            IllegalArgumentException exception, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "BAD_REQUEST", exception.getMessage(), request);
    }

    /**
     * Builds the common error response format used by this handler.
     *
     * @param status HTTP status to return
     * @param error stable application error code
     * @param message human-readable error detail
     * @param request current HTTP request
     * @return response entity containing timestamp, status, error, message, and path
     */
    private ResponseEntity<ErrorResponse> response(HttpStatus status, String error,
                                                    String message, HttpServletRequest request) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(LocalDateTime.now(), status.value(), error, message, request.getRequestURI()));
    }

    /**
     * Structured error payload returned to API clients.
     *
     * @param timestamp time at which the error was handled
     * @param status numeric HTTP status
     * @param error stable application error code
     * @param message human-readable error detail
     * @param path request URI that produced the error
     */
    public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {

        /**
         * @return time at which the error was handled
         */
        @Override
        public LocalDateTime timestamp() {
            return timestamp;
        }

        /**
         * @return numeric HTTP status
         */
        @Override
        public int status() {
            return status;
        }

        /**
         * @return stable application error code
         */
        @Override
        public String error() {
            return error;
        }

        /**
         * @return human-readable error detail
         */
        @Override
        public String message() {
            return message;
        }

        /**
         * @return request URI that produced the error
         */
        @Override
        public String path() {
            return path;
        }
    }
}
