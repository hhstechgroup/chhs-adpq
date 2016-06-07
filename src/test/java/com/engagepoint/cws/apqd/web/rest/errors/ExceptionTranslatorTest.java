package com.engagepoint.cws.apqd.web.rest.errors;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class ExceptionTranslatorTest {
    private static final String EXCEPTION_MSG = "error";
    private static final String PARAM_1 = "param1";
    private static final String PARAM_2 = "param2";

    private static ExceptionTranslator EXCEPTION_TRANSLATOR;

    @BeforeClass
    public static void beforeClass() {
        EXCEPTION_TRANSLATOR = new ExceptionTranslator();
    }

    private class MyBindException extends BindException {
        public MyBindException(Object target, String objectName) {
            super(target, objectName);
        }

        @Override
        public List<FieldError> getFieldErrors() {
            final FieldError fe1 = new FieldError(getObjectName(), PARAM_1, "");
            final FieldError fe2 = new FieldError(getObjectName(), PARAM_2, "");
            return new ArrayList<FieldError>(){{
                add(fe1);
                add(fe2);
            }};
        }
    }

    @Test
    public void testProcessConcurencyError() throws Exception {
        ErrorDTO errorDTO = EXCEPTION_TRANSLATOR.processConcurencyError(new ConcurrencyFailureException(EXCEPTION_MSG));

        assertThat(errorDTO).isNotNull();
        assertThat(errorDTO.getMessage()).isEqualTo(ErrorConstants.ERR_CONCURRENCY_FAILURE);
        assertThat(errorDTO.getDescription()).isEqualTo(EXCEPTION_MSG);
        assertThat(errorDTO.getFieldErrors()).isNull();
    }

    @Test
    public void testProcessValidationError() throws Exception {
        Method method = ExceptionTranslator.class.getDeclaredMethods()[0];
        MethodParameter methodParameter = new MethodParameter(method, 0);
        BindException bindException = new MyBindException(EXCEPTION_TRANSLATOR, EXCEPTION_TRANSLATOR.getClass().getName());
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindException);

        ErrorDTO errorDTO = EXCEPTION_TRANSLATOR.processValidationError(ex);

        assertThat(errorDTO).isNotNull();
        assertThat(errorDTO.getMessage()).isEqualTo(ErrorConstants.ERR_VALIDATION);
        assertThat(errorDTO.getDescription()).isNull();
        assertThat(errorDTO.getFieldErrors()).isNotNull();
        assertThat(errorDTO.getFieldErrors().size()).isEqualTo(2);

        FieldErrorDTO fieldError1 = errorDTO.getFieldErrors().get(0);
        assertThat(fieldError1.getObjectName()).isEqualTo(EXCEPTION_TRANSLATOR.getClass().getName());
        assertThat(fieldError1.getField()).isEqualTo(PARAM_1);
        assertThat(fieldError1.getMessage()).isNull();

        FieldErrorDTO fieldError2 = errorDTO.getFieldErrors().get(1);
        assertThat(fieldError2.getObjectName()).isEqualTo(EXCEPTION_TRANSLATOR.getClass().getName());
        assertThat(fieldError2.getField()).isEqualTo(PARAM_2);
        assertThat(fieldError2.getMessage()).isNull();
    }

    @Test
    public void testProcessParameterizedValidationError() throws Exception {
        CustomParameterizedException ex = new CustomParameterizedException(EXCEPTION_MSG, PARAM_1);
        ParameterizedErrorDTO errorDTO = EXCEPTION_TRANSLATOR.processParameterizedValidationError(ex);

        assertThat(errorDTO).isNotNull();
        assertThat(errorDTO.getMessage()).isEqualTo(EXCEPTION_MSG);
        assertThat(errorDTO.getParams()).isNotNull();
        assertThat(errorDTO.getParams().length).isEqualTo(1);
        assertThat(errorDTO.getParams()[0]).isEqualTo(PARAM_1);
    }

    @Test
    public void testProcessAccessDeniedExcpetion() throws Exception {
        AccessDeniedException ex = new AccessDeniedException(EXCEPTION_MSG);
        ErrorDTO errorDTO = EXCEPTION_TRANSLATOR.processAccessDeniedExcpetion(ex);

        assertThat(errorDTO).isNotNull();
        assertThat(errorDTO.getMessage()).isEqualTo(ErrorConstants.ERR_ACCESS_DENIED);
        assertThat(errorDTO.getDescription()).isEqualTo(EXCEPTION_MSG);
        assertThat(errorDTO.getFieldErrors()).isNull();
    }

    @Test
    public void testProcessMethodNotSupportedException() throws Exception {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException(EXCEPTION_MSG);
        ErrorDTO errorDTO = EXCEPTION_TRANSLATOR.processMethodNotSupportedException(ex);

        assertThat(errorDTO).isNotNull();
        assertThat(errorDTO.getMessage()).isEqualTo(ErrorConstants.ERR_METHOD_NOT_SUPPORTED);
        assertThat(errorDTO.getDescription()).isEqualTo(
            String.format("Request method '%s' not supported", EXCEPTION_MSG));
        assertThat(errorDTO.getFieldErrors()).isNull();
    }
}
