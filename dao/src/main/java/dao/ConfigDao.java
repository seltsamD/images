package dao;

import model.Config;

import javax.persistence.EntityManager;

public class ConfigDao extends BaseDao<Config> {

    public ConfigDao(EntityManager entityManager) {
        super(Config.class, entityManager);
    }

    public Config getByKey(String key) {
        return  entityManager.createNamedQuery(Config.GET_BY_KEY, Config.class).setParameter("k", key).getSingleResult();
    }

}
