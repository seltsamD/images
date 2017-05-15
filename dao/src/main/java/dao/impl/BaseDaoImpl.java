package dao.impl;

import dao.BaseDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class BaseDaoImpl<T> implements BaseDao<T>{
    @PersistenceContext(unitName = "punit")
    private EntityManager entityManager;

    @Override
    public void create(T newInstance) {
        entityManager.persist(newInstance);
    }

    @Override
    public T findById(Class type, Object id) {
        return (T) entityManager.find(type, id);
    }

    @Override
    public List<T> findAll(Class type) {
        return (List<T>) entityManager.createQuery("from " + type.getSimpleName(), type).getResultList();
    }

    @Override
    public void update(T object) {
        entityManager.merge(object);
    }

    @Override
    public void delete(T object) {
        entityManager.refresh(object);
    }


}
