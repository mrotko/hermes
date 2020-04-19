package pl.hermes.server.model.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface EntityDao<T, ID extends Serializable> {

    T getById(ID id);

    List<T> getAll();

    List<T> getByIds(Collection<ID> ids);

    void saveOrUpdate(T entity);

    void saveOrUpdateAll(Collection<T> entities);

    void delete(T entity);

    void deleteAll(Collection<T> entities);
}
