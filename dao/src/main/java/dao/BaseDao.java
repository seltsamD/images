package dao;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class BaseDao<T> {
    public BaseDao(Class<T> tClass, EntityManager entityManager) {
        this.tClass = tClass;
        this.entityManager = entityManager;
    }

    protected Class<T> tClass;
    protected EntityManager entityManager;

    public void create(T newInstance) {
        entityManager.persist(newInstance);
        entityManager.flush();
    }


    public T findById(Object id) {
        return (T) entityManager.find(tClass, id);
    }


    public List<T> findAll() {
        return entityManager.createQuery("from " + tClass.getSimpleName(), tClass).getResultList();
    }

    public void update(T object) {
        entityManager.merge(object);
    }

    public void delete(T object) {
        entityManager.remove(object);
    }


}
