package factory;

import model.Project;
import model.User;
import repository.ProjectRepository;

public class CDIProjectRepositoryFactory {
    private String rootPath;


    public CDIProjectRepositoryFactory(String rootPath) {
        this.rootPath = rootPath;
    }

    public ProjectRepository create(User user, Project project) {
        return new ProjectRepository(user.getId(), rootPath, project.getProjectName());
    }
}
