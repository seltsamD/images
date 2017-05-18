package dao;


import model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class UserDao extends BaseDao<User> {

    public UserDao(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public long getIdByUsername(String username) {
        return  entityManager.createNamedQuery(User.GET_ID_BY_USERNAME, Long.class).setParameter("user_name", username).getSingleResult();
    }

    public User save(User user){
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
}
