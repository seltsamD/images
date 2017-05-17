package dao;


import model.Project;

import javax.persistence.EntityManager;
import java.util.List;

public class ProjectDao extends BaseDao<Project> {

    public ProjectDao(EntityManager entityManager) {
        super(Project.class, entityManager);
    }

    public long save(Project obj) {
        entityManager.persist(obj);
        entityManager.flush();
        return obj.getId();
    }

    public List<Project> getByUserName(String username) {
        return entityManager.createNamedQuery(Project.GET_BY_USERNAME, Project.class).setParameter("name", username).getResultList();
    }

    public List<Project> getByUser(long user_id) {
        return entityManager.createNamedQuery(Project.GET_BY_USER, Project.class).setParameter("id", user_id).getResultList();
    }

    public long getUserByProject(long id) {
        return entityManager.createNamedQuery(Project.GET_USER_BY_PROJECT, Long.class).setParameter("id", id).getSingleResult();
    }
}
