package dao;


import javax.ejb.Remote;
import java.io.Serializable;
import java.util.List;

@Remote
public interface BaseDao <T> {

    void create(T newInstance);

    T findById(Class t, Object id);

    List<T> findAll(Class t);

    void update(T transientObject);

    void delete(T persistentObject);
}