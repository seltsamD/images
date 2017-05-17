package dao;


import constants.ProjectConstants;
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

    public List<Project> getByUserName(String username){
        return entityManager.createNamedQuery(ProjectConstants.getByUsername, Project.class).setParameter("name", username).getResultList();
    }

    public List<Project> getByUser(long user_id) {
        return entityManager.createNamedQuery(ProjectConstants.getByUser, Project.class).setParameter("id", user_id).getResultList();
    }

    public long getUserByProject(long id){
        return entityManager.createNamedQuery(ProjectConstants.getUserByProject,  Long.class).setParameter("id", id).getSingleResult();
    }
}
