package test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

/**
 * Created by dnakhod on 12.05.2017.
 */
public class DaoTest {
    @PersistenceUnit(name = "punit")
    EntityManager manager;

}
