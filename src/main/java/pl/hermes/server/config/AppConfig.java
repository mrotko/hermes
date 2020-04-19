package pl.hermes.server.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import pl.hermes.server.util.LazyValue;

import java.util.Locale;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class AppConfig {

    public static final Locale LOCALE = Locale.forLanguageTag("pl-PL");

    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Europe/Warsaw");

    private static final LazyValue<Configuration> configuration = LazyValue.from(() -> {
        var params = new Parameters();
        var builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(params.properties().setFileName("application.properties"));
        try {
            return builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    });

    public static Configuration getConfiguration() {
        return configuration.get();
    }
}
