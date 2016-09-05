package tds.session.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tds.common.web.exceptions.NotFoundException;
import tds.common.web.resources.ExceptionMessageResource;

@ControllerAdvice
class ExceptionAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    ResponseEntity<ExceptionMessageResource> handleNotFoundException(final NotFoundException ex) {
        return new ResponseEntity<>(
                new ExceptionMessageResource(HttpStatus.NOT_FOUND.toString(), ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ResponseEntity<ExceptionMessageResource> handleException(final Exception ex) {
        LOG.error("Unexpected error", ex);

        return new ResponseEntity<>(
                new ExceptionMessageResource(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
