package pl.hermes.server.db;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import pl.hermes.server.util.LazyValue;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.hibernate.cfg.AvailableSettings.*;
import static pl.hermes.server.config.AppConfig.getConfiguration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtil {

    private static final LazyValue<SessionFactory> sessionFactory = LazyValue.from(() -> {
        var configuration = new Configuration();
        var settings = hibernateSettings();
        configuration.setProperties(settings);
        var registry = new StandardServiceRegistryBuilder().applySettings(settings).build();

        return annotatedClasses(configuration).buildSessionFactory(registry);
    });

    private static Properties hibernateSettings() {
        Properties settings = new Properties();

        settings.put(URL, getConfiguration().getString("hibernate.connection.url"));
        settings.put(USER, getConfiguration().getString("hibernate.connection.username"));
        settings.put(PASS, getConfiguration().getString("hibernate.connection.password"));
        settings.put(DRIVER, getConfiguration().getString("hibernate.connection.driver_class"));
        settings.put(DIALECT, getConfiguration().getString("hibernate.dialect"));
        settings.put(CACHE_PROVIDER_CONFIG, getConfiguration().getString("hibernate.cache.provider_configuration_file_resource_path", "org.hibernate.cache.EhCacheProvider"));

        settings.put(SHOW_SQL, getConfiguration().getString("hibernate.show_sql", "false"));
        settings.put(CURRENT_SESSION_CONTEXT_CLASS, getConfiguration().getString("hibernate.current_session_context_class", "thread"));
        settings.put(HBM2DDL_AUTO, getConfiguration().getString("hibernate.hbm2ddl.auto", "validate"));

        return settings;
    }

    private static Configuration annotatedClasses(Configuration configuration) {
        Set<Class<?>> entityClasses = new HashSet<>(getEntityClasses("pl.hermes.server.model.domain"));

        for (final Class<?> entityClass : entityClasses) {
            configuration.addAnnotatedClass(entityClass);
        }

        return configuration;
    }

    private static Set<Class<?>> getEntityClasses(String packageName) {
        var reflections = new Reflections(packageName);
        var entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
        entityClasses.addAll(reflections.getTypesAnnotatedWith(Embeddable.class));
        return entityClasses;
    }


    public static SessionFactory getSessionFactory() {
        return sessionFactory.get();
    }
}
