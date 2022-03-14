package io.github.mityavasilyev.springreactbarapp.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {Exception.class})
    public ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        log.warn("Failed to verify JWT token");
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

//    /**
//     * Handles exceptions
//     *
//     * @param ex
//     * @param request
//     * @param response
//     * @return
//     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handle(Exception ex,
//                                         HttpServletRequest request, HttpServletResponse response) {
//        log.info("Controller Error: {}", ex.getMessage());
//        if (ex instanceof NullPointerException) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        if (ex instanceof DataIntegrityViolationException) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(ex.getMessage());
//        }
//        if (ex instanceof DataNotFoundException) {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(ex.getMessage());
//        }
//        if (ex instanceof NotEnoughProductException) {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_ACCEPTABLE)
//                    .body(ex.getMessage());
//        }
//        if (ex instanceof UnitMismatchException) {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_ACCEPTABLE)
//                    .body(ex.getMessage());
//        }
//        if (ex instanceof InvalidIdException) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body("Invalid Id");
//        }
//
//        log.error(ex.getMessage());
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
}
