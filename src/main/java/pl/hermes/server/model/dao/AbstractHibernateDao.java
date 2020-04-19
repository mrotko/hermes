package pl.hermes.server.model.dao;

import org.hibernate.Session;
import pl.hermes.server.db.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static pl.hermes.server.db.TransactionSupport.inTransaction;

abstract class AbstractHibernateDao<T, ID extends Serializable> implements EntityDao<T, ID> {

    private final Class<T> clazz;

    public AbstractHibernateDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected Session getCurrentSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    @Override
    public T getById(ID id) {
        return inTransaction(() -> getCurrentSession().byId(clazz).load(id));
    }

    @Override
    public List<T> getAll() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        CriteriaQuery<T> all = cq.select(rootEntry);
        return getCurrentSession().createQuery(all).getResultList();
    }

    @Override
    public List<T> getByIds(Collection<ID> ids) {
        return getCurrentSession().byMultipleIds(clazz).multiLoad(new ArrayList<>(ids));
    }

    @Override
    public void saveOrUpdate(T entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public void saveOrUpdateAll(Collection<T> entities) {
        entities.forEach(getCurrentSession()::saveOrUpdate);
    }

    @Override
    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public void deleteAll(Collection<T> entities) {
        entities.forEach(getCurrentSession()::delete);
    }
}
