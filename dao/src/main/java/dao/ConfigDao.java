package dao;

import model.db.Config;

import javax.persistence.EntityManager;
import java.util.List;

public class ConfigDao extends BaseDao<Config> {

    public ConfigDao(EntityManager entityManager) {
        super(Config.class, entityManager);
    }

    public Config getByKey(String key) {
        List<Config> result = entityManager.createNamedQuery(Config.GET_BY_KEY, Config.class).setParameter("k", key).getResultList();
        if(result.size() == 0)
            return null;
        return result.get(0);
    }

}
