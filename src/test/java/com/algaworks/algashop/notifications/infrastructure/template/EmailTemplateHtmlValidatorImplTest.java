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

    @Test
    void givenHtmlWithScriptTag_whenValidateSyntax_shouldThrowInvalidTemplateException() {
        Assertions.assertThatExceptionOfType(InvalidTemplateHtmlException.class)
                .isThrownBy(() -> validator.validateSyntax("<p>Hello</p><script>console.log('hi')/script"));
    }

    @Test
    void givenHtmlWithInlineEventHandler_whenValidateSyntax_shouldThrowInvalidTemplateException() {
        Assertions.assertThatExceptionOfType(InvalidTemplateHtmlException.class)
                .isThrownBy(() -> validator.validateSyntax("<p onclick=\"alert(1)\">Hello</p>"));
    }

    @Test
    void givenHtmlWithThymeleafAttribute_whenValidateSyntax_shouldThrowInvalidTemplateException() {
        Assertions.assertThatCode(() -> validator.validateSyntax("<p th:text=\"${recipient.name}\">Alex</p>"))
                .doesNotThrowAnyException();
    }

    @Test
    void givenHtmlWithJavascriptUrl_whenValidateSyntax_shouldThrowInvalidTemplateException() {
        Assertions.assertThatExceptionOfType(InvalidTemplateHtmlException.class)
                .isThrownBy(() -> validator.validateSyntax("<a href=\"javascript:alert(1)\">link</a>"));
    }

}