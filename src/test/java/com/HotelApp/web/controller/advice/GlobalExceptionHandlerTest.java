package com.HotelApp.web.controller.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.HotelApp.common.constants.FailConstants.ERRORS;
import static com.HotelApp.common.constants.SuccessConstants.SUCCESS;
import static com.HotelApp.service.constants.TestConstants.DEFAULT_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {
    private static final String FIELD_NAME = "fieldName";
    private static final String FIELD_ERROR = "Field must not be empty";
    private static final String FIELD = "field";

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleValidationException() {
        Path path = Mockito.mock(Path.class);
        Mockito.when(path.toString()).thenReturn(FIELD_NAME);

        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn(FIELD_ERROR);

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException ex = new ConstraintViolationException(violations);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleValidationException(ex);
        Map<String, Object> body = response.getBody();
        List<Map<String, Object>> errors = (List<Map<String, Object>>) body.get(ERRORS);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals(false, body.get(SUCCESS));
        assertEquals(FIELD_NAME, errors.get(0).get(FIELD));
        assertEquals(FIELD_ERROR, errors.get(0).get(DEFAULT_MESSAGE));
    }
}