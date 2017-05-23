package repository;

import model.db.Project;
import model.db.User;

public class ProjectRepositoryFactory {
    private String rootPath;


    public ProjectRepositoryFactory(String rootPath) {
        this.rootPath = rootPath;
    }

    public ProjectRepository create(User user, Project project) {
        return new ProjectRepository(user.getId(), rootPath, project.getProjectName());
    }
}
