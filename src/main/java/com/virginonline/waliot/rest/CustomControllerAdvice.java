package com.virginonline.waliot.rest;

import com.virginonline.waliot.exception.CoordinatesException;
import com.virginonline.waliot.exception.ExceptionBody;
import com.virginonline.waliot.exception.LocationNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomControllerAdvice {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ExceptionBody> handle(IllegalArgumentException e) {
    return new ResponseEntity<>(new ExceptionBody(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CoordinatesException.class)
  public ResponseEntity<ExceptionBody> handle(CoordinatesException e) {
    return new ResponseEntity<>(new ExceptionBody(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ExceptionBody> handle(Exception e) {
    return new ResponseEntity<>(
        new ExceptionBody(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(LocationNotFound.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ExceptionBody> handle(LocationNotFound e) {
    return new ResponseEntity<>(new ExceptionBody(e.getMessage()), HttpStatus.NOT_FOUND);
  }
}
