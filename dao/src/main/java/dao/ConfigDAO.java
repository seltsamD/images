package dao;

import model.Config;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ConfigDAO {
    @PersistenceContext(unitName = "punit")
    private EntityManager dao;

    public List<Config> findAll(){
        return dao.createQuery("from Config").getResultList();
    }


    public void save(Config config){
        dao.persist(config);
    }

    public Config getByKey(String key){
        return (Config) dao.createQuery("from Config c where c.key = :k").setParameter("k", key).getSingleResult();
    }

}
