package com.algaworks.algashop.notifications.infrastructure.template;

import com.algaworks.algashop.notifications.application.email.InvalidTemplateHtmlException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTemplateHtmlValidatorImplTest {

    private final EmailTemplateHtmlValidatorImpl validator = new EmailTemplateHtmlValidatorImpl();

    @Test
    void givenWellFormedHTML_whenValidateSyntax_shouldNotThrowErrors() {
        Assertions.assertThatCode(() -> validator.validateSyntax("<p>Hello</p>")).doesNotThrowAnyException();
    }

    @Test
    void givenMalformedHTML_whenValidateSyntax_shouldThrowInvalidTemplateException() {
        Assertions.assertThatExceptionOfType(InvalidTemplateHtmlException.class)
                .isThrownBy(() -> validator.validateSyntax("</div><p>Hello</p></div>"));
    }

}