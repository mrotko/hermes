package pl.hermes.server;

import org.hibernate.SessionFactory;
import pl.hermes.server.db.HibernateUtil;
import ratpack.server.RatpackServer;

public class HermesServer {

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        RatpackServer.start(server ->
                server.handlers(chain -> chain
                        .get("hello-world", ctx -> ctx.render("Hello world"))
                ));
    }
}
