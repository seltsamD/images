package dao;


import model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class UserDao extends BaseDao<User> {

    public UserDao(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public long getIdByUsername(String username) {
        //TODO: call query name from User entity
        return  entityManager.createNamedQuery("User.getIdByUsername", Long.class).setParameter("user_name", username).getSingleResult();
    }

    public User save(User user){
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
}
