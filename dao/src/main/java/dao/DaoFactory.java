package dao;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DaoFactory {

    @PersistenceContext(unitName = "punit")
    private EntityManager dao;

    @Produces
    public ProjectDao produceProjectDao() {
        return new ProjectDao(dao);
    }

    @Produces
    public ConfigDao produceConfigDao() {
        return new ConfigDao(dao);
    }

    @Produces
    public UserDao produceUserDao() {
        return new UserDao(dao);
    }
}
