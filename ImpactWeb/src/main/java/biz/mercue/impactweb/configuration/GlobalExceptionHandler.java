package biz.mercue.impactweb.configuration;

import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.CustomException;
import biz.mercue.impactweb.util.ExceptionResponseBody;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private Logger log = Logger.getLogger(this.getClass().getName());
    private static ExceptionResponseBody responseBody = new ExceptionResponseBody();

    private HttpStatus _200 = HttpStatus.OK;
    private HttpStatus _500 = HttpStatus.INTERNAL_SERVER_ERROR;

    // custom exception + json
    @ExceptionHandler(CustomException.TokenNullException.class)
    public ResponseEntity TokenNullException(CustomException.TokenNullException e) {
        log.error("TokenNullException: " + e.getMessage());
        responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        return new ResponseEntity<>(responseBody, _200);
    }

    @ExceptionHandler(CustomException.CanNotFindDataException.class)
    public ResponseEntity CanNotFindDataException(CustomException.CanNotFindDataException e) {
        log.error("CanNotFindDataException: " + e.getMessage());
        responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
        return new ResponseEntity<>(responseBody, _200);
    }

    // java exception
    @ExceptionHandler(IOException.class)
    public ResponseEntity IOException(IOException e) {
        log.error("IOException: " + e.getMessage());
        responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
        return new ResponseEntity<>(responseBody, _200);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity Exception(Exception e) {
        log.error("Exception", e);
        responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
        return new ResponseEntity<>(responseBody, _200);
    }
}
