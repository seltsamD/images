package dao;

import model.Config;

import javax.persistence.EntityManager;

public class ConfigDao extends BaseDao<Config> {

    public ConfigDao(EntityManager entityManager) {
        super(Config.class, entityManager);
    }

    public Config getByKey(String key) {
        //TODO: move query to Config entity
        return  entityManager.createQuery("from Config c where c.key = :k", Config.class).setParameter("k", key).getSingleResult();
    }

}
