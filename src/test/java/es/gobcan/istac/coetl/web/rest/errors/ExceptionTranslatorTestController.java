package es.gobcan.istac.coetl.web.rest.errors;

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

import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ParameterizedErrorItem;
import es.gobcan.istac.coetl.errors.ParameterizedErrorItemBuilder;

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
        throw new CustomParameterizedExceptionBuilder().message("test parameterized error").code("error.test", "param0_value", "param1_value").build();
    }

    @GetMapping("/test/parameterized-error2")
    public void parameterizedError2() {
        List<ParameterizedErrorItem> errorItems = new ArrayList<>();
        errorItems.add(new ParameterizedErrorItemBuilder().message("message1").code("code1").build());
        errorItems.add(new ParameterizedErrorItemBuilder().message("message2").code("code2", "param_code2").build());
        errorItems.add(new ParameterizedErrorItemBuilder().message("message3").code("code3", "param1_code3", "param2_code3").build());
        String message = "test parameterized error";
        String code = "error.test";
        throw new CustomParameterizedExceptionBuilder().message(message).code(code, "param0_value", "param1_value").errorItems(errorItems).build();
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
