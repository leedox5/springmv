package kr.leedox;

import kr.leedox.repository.WordRepository;
import kr.leedox.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component("messageSource")
public class DBMessageSource extends AbstractMessageSource {

    @Autowired
    private WordService wordService;

    private static final String DEFAULT_LOCALE_CODE = "en";
    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        Lang lang = wordService.getLang(code, locale.getLanguage());
        return new MessageFormat(lang.getContent(), locale);
    }
}
