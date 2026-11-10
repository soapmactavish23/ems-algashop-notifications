package com.algaworks.algashop.notifications.infrastructure.template;

import com.algaworks.algashop.notifications.application.email.EmailTemplateHtmlValidator;

import com.algaworks.algashop.notifications.application.email.InvalidTemplateHtmlException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class EmailTemplateHtmlValidatorImpl implements EmailTemplateHtmlValidator {

	private static final int MAX_VIOLATIONS = 10;

	private static final Set<String> FORBIDDEN_TAGS = Set.of("script", "iframe", "object", "embed");

	@Override
	public void validateSyntax(String html) {
		Objects.requireNonNull(html);
		if(html.isEmpty()) {
			throw new IllegalArgumentException("html must not be empty");
		}

		Parser parser = Parser.htmlParser().setTrackErrors(MAX_VIOLATIONS);
		Document document = Jsoup.parse(html, parser);

		List<String> violations = new ArrayList<>();

		for(ParseError error : parser.getErrors()) {
			violations.add(String.format("Malformed HTML at position %s: %s",
					error.getPosition(), error.getErrorMessage()));
		}

		for(Element element : document.getAllElements()) {
			checkElement(element, violations);
		}

		if(!violations.isEmpty()) {
			throw new InvalidTemplateHtmlException(violations);
		}
	}

    private void checkElement(Element element, List<String> violations) {
		String tag = element.normalName();
		if(FORBIDDEN_TAGS.contains(tag)) {
			violations.add(String.format("Element <%s> is not allowed", tag));
		}
	}
}
