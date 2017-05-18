package service;

import cdi.ProjectRepository;
import cdi.CDIProjectsFactory;
import cdi.ProjectRepositoryFactory;
import dao.ProjectDao;
import dao.UserDao;
import model.Project;
import model.User;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import java.io.*;
import java.util.List;

import static constants.ProjectConstants.LOCATION_FOLDER;

@Stateless
public class ProjectService {

    @Inject
    ProjectDao projectDao;

    @Inject
    UserDao userDao;


    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/jms/queue/projectQueue")
    private Queue queue;

    @Inject
    private ProjectRepositoryFactory projectsFactory;


    public List<Project> findAll() {
        return projectDao.findAll();
    }

    public List<Project> getByUserName(String username) {
        return projectDao.getByUserName(username);
    }

    public void delete(long id) {
        Project project = projectDao.findById(id);


        File folder = new File(LOCATION_FOLDER + "/" + project.getUser().getId() + "/" + project.getProjectName());
        deleteFolder(folder);
        File file = new File(LOCATION_FOLDER + "/" + project.getUser().getId() + "/" + project.getProjectName() + ".zip");
        file.delete();
        projectDao.delete(project);
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public void save(long userId, String fileName, InputStream inputStream) {
        Project project = projectDao.save(fileName.substring(0, fileName.indexOf(".")), userDao.findById(userId));
        User user = userDao.findById(userId);

        ProjectRepository projectRepository = projectsFactory.create(user, project);
        projectRepository.saveProjectArchive(inputStream);

    }

    public void savePreview(String user_id, String projectName){
        User user = userDao.findById(user_id);
        Project project = projectDao.getByProjectName(projectName);
        projectsFactory.create(user, project).savePreview();
    }


    public File callPreview(String username, String projectName){
        User user = userDao.findByUsername(username);
        Project project = projectDao.getByProjectName(projectName);
        return projectsFactory.create(user, project).callPreview();
    }
}
