package pl.hermes.server;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;
import static pl.hermes.server.AppConfig.getConfiguration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUnit {

    private static final LazyValue<SessionFactory> sessionFactory = LazyValue.from(() -> {
        Configuration configuration = new Configuration();

        Properties settings = new Properties();
        settings.put(URL, getConfiguration().getString("hermes.db.url"));
        settings.put(USER, getConfiguration().getString("hermes.db.uername"));
        settings.put(PASS, getConfiguration().getString("hermes.db.password"));
        settings.put(DRIVER, "org.postgresql.Driver");
        settings.put(DIALECT, "org.hibernate.dialect.PostgreSQL95Dialect");
        settings.put(CACHE_PROVIDER_CONFIG, "org.hibernate.cache.EhCacheProvider");

        settings.put(SHOW_SQL, "true");
        settings.put(CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(HBM2DDL_AUTO, "create-drop");

        configuration.setProperties(settings);
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(settings).build();

        return configuration.buildSessionFactory(registry);
    });

    public static SessionFactory getSessionFactory() {
        return sessionFactory.get();
    }
}
