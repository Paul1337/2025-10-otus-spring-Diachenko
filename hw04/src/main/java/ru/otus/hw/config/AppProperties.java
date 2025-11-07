package ru.otus.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.shell.jline.PromptProvider;

import java.util.Locale;
import java.util.Map;

@Setter
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig, PromptProvider {
    @Getter
    private int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    private Map<String, String> fileNameByLocaleTag;

    public void setLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("quiz-app:> ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN));
    }
}
