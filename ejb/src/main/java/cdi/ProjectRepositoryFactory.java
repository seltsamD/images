package cdi;

import model.Project;
import model.User;

//TODO; move to repository package inside DAO module, its also DAO just for files
public class ProjectRepositoryFactory {
    private String rootPath;


    public ProjectRepositoryFactory(String rootPath) {
        this.rootPath = rootPath;
    }

    public ProjectRepository create(User user, Project project) {
        return new ProjectRepository(user.getId(), rootPath, project.getProjectName());
    }
}
