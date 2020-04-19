package pl.hermes.server.model.service;

import pl.hermes.server.model.dao.EntityDao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static pl.hermes.server.db.TransactionSupport.inTransaction;

abstract class AbstractHibernateService <T, ID extends Serializable> implements EntityService<T, ID> {

    private final EntityDao<T, ID> dao;

    public AbstractHibernateService(EntityDao<T, ID> dao) {
        this.dao = dao;
    }

    @Override
    public T getById(ID id) {
        return inTransaction(() -> dao.getById(id));
    }

    @Override
    public List<T> getAll() {
        return inTransaction(dao::getAll);
    }

    @Override
    public List<T> getByIds(Collection<ID> ids) {
        return inTransaction(() -> dao.getByIds(ids));
    }

    @Override
    public void saveOrUpdate(T entity) {
        inTransaction(() -> dao.saveOrUpdate(entity));
    }

    @Override
    public void saveOrUpdateAll(Collection<T> entities) {
        inTransaction(() -> dao.saveOrUpdateAll(entities));
    }

    @Override
    public void delete(T entity) {
        inTransaction(() -> dao.delete(entity));
    }

    @Override
    public void deleteAll(Collection<T> entities) {
        inTransaction(() -> dao.deleteAll(entities));
    }
}
