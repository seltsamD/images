package dao.impl;

import dao.ConfigDao;
import model.Config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ConfigDaoImpl extends BaseDaoImpl<Config> implements ConfigDao {

    @PersistenceContext(unitName = "punit")
    private EntityManager dao;

    public Config getByKey(String key) {
        return (Config) dao.createQuery("from Config c where c.key = :k").setParameter("k", key).getSingleResult();
    }

}
