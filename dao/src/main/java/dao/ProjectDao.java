package dao;


import model.db.Project;
import model.db.User;

import javax.persistence.EntityManager;
import java.util.List;

public class ProjectDao extends BaseDao<Project> {

    public ProjectDao(EntityManager entityManager) {
        super(Project.class, entityManager);
    }

    public Project save(String projectName, User user) {
        Project project = new Project();
        project.setProjectName(projectName);
        project.setUser(user);
        entityManager.persist(project);
        entityManager.flush();
        return project;
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

    public Project getByProjectName(String name){
        return entityManager.createNamedQuery(Project.GET_BY_PROJECTNAME, Project.class).setParameter("name", name).getSingleResult();
    }

}
