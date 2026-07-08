package kr.leedox.service;

import kr.leedox.entity.SiteMessage;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.repository.SiteMessageRepository;
import kr.leedox.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MessageService {
    private static final String DEFAULT_LOCALE = "ko";
    private static final String EN_LEGACY_WORD = "202404.002";
    private static final String KO_LEGACY_WORD = "202404.003";

    private final SiteMessageRepository siteMessageRepository;
    private final WordRepository wordRepository;
    private final Map<String, String> messageCache = new ConcurrentHashMap<>();

    public String getMessage(String code, Locale locale) {
        String language = normalize(locale);
        return getMessage(code, language)
                .or(() -> getMessage(code, DEFAULT_LOCALE))
                .or(() -> getMessage(code, "en"))
                .orElse(code);
    }

    public void clearCache() {
        messageCache.clear();
    }

    private Optional<String> getMessage(String code, String language) {
        String cacheKey = language + ":" + code;
        String cached = messageCache.get(cacheKey);
        if(cached != null) {
            return Optional.of(cached);
        }

        Optional<String> message = siteMessageRepository.findByCodeAndLocale(code, language)
                .map(SiteMessage::getContent)
                .filter(content -> !content.isBlank())
                .or(() -> getLegacyMessage(code, language));

        message.ifPresent(content -> messageCache.put(cacheKey, content));
        return message;
    }

    private Optional<String> getLegacyMessage(String code, String language) {
        String word = "en".equals(language) ? EN_LEGACY_WORD : KO_LEGACY_WORD;
        List<Wordbook> wordbooks = wordRepository.findByWord(word);

        if(wordbooks.isEmpty()) {
            return Optional.empty();
        }

        for(WordMeaning wordMeaning : wordbooks.get(0).getWordMeanings()) {
            String meaning = wordMeaning.getMeaning();
            if(meaning == null) {
                continue;
            }

            String[] values = meaning.split("\\|", 2);
            if(values.length == 2 && code.equals(values[0]) && !values[1].isBlank()) {
                return Optional.of(values[1]);
            }
        }

        return Optional.empty();
    }

    private String normalize(Locale locale) {
        if(locale == null || locale.getLanguage() == null || locale.getLanguage().isBlank()) {
            return DEFAULT_LOCALE;
        }
        return locale.getLanguage();
    }
}
