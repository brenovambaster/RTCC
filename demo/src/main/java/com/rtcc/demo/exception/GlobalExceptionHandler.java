package com.rtcc.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(EntityDeletionException.class)
    public ResponseEntity<ErrorResponse> handleEntityDeletionException(EntityDeletionException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

//
//    @ExceptionHandler(CourseDeletionException.class)
//    public ResponseEntity<ErrorResponse> handleCourseDeletionException(CourseDeletionException ex) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                ex.getMessage(),
//                HttpStatus.CONFLICT.value());
//        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(ProfessorDeletionException.class)
//    public ResponseEntity<ErrorResponse> handleProfessorDeletionException(ProfessorDeletionException ex) {
//        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
//        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
//    }

}
