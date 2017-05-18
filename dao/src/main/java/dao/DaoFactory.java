package dao;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//TODO: move factory to factory package inside ejb module
//TODO: let names of factories for CDI container starts with Cdi prefix
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
