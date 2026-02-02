package com.ems.ems.exception;


import com.ems.ems.dto.response.ResponseDto;
import com.ems.ems.dto.response.ValidationErrorResponseDto;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> ResourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        String message =
                ex.getResource() + " ("+ex.getField()+") : With Value " + ex.getValue() + " Not Found !";
        ResponseDto response = new ResponseDto();
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SomethingWentWrongException.class)
    public ResponseEntity<ResponseDto> SomethingWentWrongExceptionHandler(SomethingWentWrongException ex){
        String message =
                ex.getMessage();
        ResponseDto response = new ResponseDto();
        response.setMessage(message);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e->
                errors.put(e.getField(),e.getDefaultMessage())
        );
        ValidationErrorResponseDto response = new ValidationErrorResponseDto();
        response.setErrors(errors);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<?> UnexpectedTypeExceptionHandler(UnexpectedTypeException ex){
        String message = ex.getCause().getMessage();
        ResponseDto response = new ResponseDto();
        response.setMessage(message);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
