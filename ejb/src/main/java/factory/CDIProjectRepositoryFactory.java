package factory;

import model.Project;
import model.User;
import repository.ProjectRepository;

//TODO: replace this to repository package inside dao, rename it to ProjectRepositoryFactory
// this class is not maneged by CDI container
public class CDIProjectRepositoryFactory {
    private String rootPath;


    public CDIProjectRepositoryFactory(String rootPath) {
        this.rootPath = rootPath;
    }

    public ProjectRepository create(User user, Project project) {
        return new ProjectRepository(user.getId(), rootPath, project.getProjectName());
    }
}
