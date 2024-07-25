package org.andrelucs.SpringChatServer.exceptions;

import org.andrelucs.SpringChatServer.model.dto.ExceptionDTO;
import org.andrelucs.SpringChatServer.model.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionDTO> handleGeneralException(Exception e, WebRequest request) {
        var res = new ExceptionDTO(e.getMessage(),
                new Date(),
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ExceptionDTO> handleGeneralException(NotFoundException e, WebRequest request) {
        var res = new ExceptionDTO(e.getMessage(),
                new Date(),
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

}
