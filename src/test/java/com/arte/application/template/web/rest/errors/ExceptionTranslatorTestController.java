package com.arte.application.template.web.rest.errors;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionTranslatorTestController {

    @GetMapping("/test/concurrency-failure")
    public void concurrencyFailure() {
        throw new ConcurrencyFailureException("test concurrency failure");
    }

    @PostMapping("/test/method-argument")
    public void methodArgument(@Valid @RequestBody TestDTO testDTO) {
    }

    @GetMapping("/test/parameterized-error")
    public void parameterizedError() {
        throw new CustomParameterizedException("test parameterized error", "error.test", "param0_value", "param1_value");
    }

    @GetMapping("/test/parameterized-error2")
    public void parameterizedError2() {
        List<ParameterizedErrorItem> errorItems = new ArrayList<>();
        errorItems.add(new ParameterizedErrorItem("message1", "code1"));
        errorItems.add(new ParameterizedErrorItem("message2", "code2", "param_code2"));
        errorItems.add(new ParameterizedErrorItem("message3", "code3", "param1_code3", "param2_code3"));
        String message = "test parameterized error";
        String code = "error.test";
        throw new CustomParameterizedException(message, code, errorItems, "param0_value", "param1_value");
    }

    @GetMapping("/test/access-denied")
    public void accessdenied() {
        throw new AccessDeniedException("test access denied!");
    }

    @GetMapping("/test/response-status")
    public void exceptionWithReponseStatus() {
        throw new TestResponseStatusException();
    }

    @GetMapping("/test/internal-server-error")
    public void internalServerError() {
        throw new RuntimeException();
    }

    public static class TestDTO {

        @NotNull
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "test response status")
    @SuppressWarnings("serial")
    public static class TestResponseStatusException extends RuntimeException {
    }

}
