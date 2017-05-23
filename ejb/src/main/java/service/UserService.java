package service;

import dao.UserDao;
import model.db.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UserService {

    @Inject
    private UserDao userDao;

    public long getIdByUsername(String username) {
        return userDao.getIdByUsername(username);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User save(User user) {
        return userDao.save(user);
    }

    public User getById(long id){
        return userDao.findById(id);
    }
}
