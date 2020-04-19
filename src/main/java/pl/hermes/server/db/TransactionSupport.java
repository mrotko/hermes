package pl.hermes.server.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Supplier;

public class TransactionSupport {

    private TransactionSupport() {}

    public static <T> T inTransaction(Supplier<T> supplier) {
        Transaction transaction = null;
        Session currentSession = null;
        try  {
            currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = currentSession.getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            } else {
                currentSession.joinTransaction();
            }
            T result = supplier.get();
            if (!currentSession.isJoinedToTransaction()) {
                transaction.commit();
            }
            return result;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (currentSession != null && !currentSession.isJoinedToTransaction()) {
                currentSession.close();
            }
        }
    }

    public static void inTransaction(Runnable runnable) {
        inTransaction(() -> {
            runnable.run();
            return null;
        });
    }
}
