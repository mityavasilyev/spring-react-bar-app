package io.github.mityavasilyev.springreactbarapp.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ExceptionUtils {
    public static final String JSON_FIELD_ERROR = "error_message";

    /**
     * @param errorMessage Message describing the problem
     * @param extraObject  Some extra payload if necessary
     * @return standardized error body
     */
    public static Map buildErrorBody(String errorMessage, Object extraObject) {
        Map<String, String> body = new HashMap<>();

        body.put("error_message", errorMessage);
        try {
            if (extraObject != null) body.put("more_info", new ObjectMapper().writeValueAsString(extraObject));
        } catch (JsonProcessingException e) {
            log.error("Failed to put extra payload to an exception message: {}", extraObject);
        }
        return body;
    }

    public static Map buildErrorBody(String errorMessage) {
        return buildErrorBody(errorMessage, null);
    }
}
