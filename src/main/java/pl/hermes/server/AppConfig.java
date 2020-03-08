package pl.hermes.server;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfig {

    private static final LazyValue<Configuration> configuration = LazyValue.from(() -> {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
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
