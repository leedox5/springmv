package kr.leedox;

import kr.leedox.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component("messageSource")
@RequiredArgsConstructor
public class DBMessageSource extends AbstractMessageSource {

    private final MessageService messageService;

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String message = messageService.getMessage(code, locale);
        return new MessageFormat(message, locale);
    }
}
