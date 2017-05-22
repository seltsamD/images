package factory;

import dao.ConfigDao;
import dao.ProjectDao;
import dao.UserDao;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


//TODO: move it to dao module dao package, remove jpa dependency
// I had asked to move it to ejb, but forgot about JPA dependency, =(
public class CDIDaoFactory {

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
