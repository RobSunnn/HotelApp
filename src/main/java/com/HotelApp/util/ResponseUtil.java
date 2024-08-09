package com.HotelApp.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.HotelApp.common.constants.FailConstants.ERRORS;
import static com.HotelApp.common.constants.SuccessConstants.REDIRECT_URL;
import static com.HotelApp.common.constants.SuccessConstants.SUCCESS;

public class ResponseUtil {
    private static final String FIELD = "field";
    private static final String DEFAULT_MESSAGE = "defaultMessage";

    public static ResponseEntity<?> genericSuccessResponse(String redirectUrl) {
        Map<String, Object> response = new HashMap<>();
        response.put(SUCCESS, true);
        response.put(REDIRECT_URL, redirectUrl);
        return ResponseEntity.ok().body(response);
    }

    public static ResponseEntity<?> genericFailResponse(BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        response.put(SUCCESS, false);
        response.put(ERRORS, bindingResult.getAllErrors());
        return ResponseEntity.badRequest().body(response);
    }

    public static ResponseEntity<Map<String, Object>> genericFailResponse(Map<String, String> errorMessages) {
        Map<String, Object> response = new HashMap<>();
        response.put(SUCCESS, false);

        List<Map<String, String>> errors = new ArrayList<>();
        for (Map.Entry<String, String> entry : errorMessages.entrySet()) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put(FIELD, entry.getKey());
            errorDetails.put(DEFAULT_MESSAGE, entry.getValue());
            errors.add(errorDetails);
        }

        response.put(ERRORS, errors);
        return ResponseEntity.badRequest().body(response);
    }
}
