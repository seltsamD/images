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

    public List<Project> getByUser(long id) {
        return entityManager.createNamedQuery("Project.getByUser", Project.class).setParameter("id", id).getResultList();
    }

    public List<Object> findAllWithUser() {
        return entityManager.createNamedQuery("Project.findAllWithUser").getResultList();
    }

    public long getUserByProject(long id){
        return entityManager.createNamedQuery("Project.getUserByProject",  Long.class).setParameter("id", id).getSingleResult();
    }
}
