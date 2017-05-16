package service;

import dao.ProjectDao;
import dao.UserDao;
import model.Project;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.*;
import java.util.List;

@Stateless
public class ProjectService {

    @Inject
    ProjectDao projectDao;

    private static final String LOCATION_FOLDER = "C:/Users/dnakhod/Documents/";

    @Inject
    UserDao userDao;

    public List<Object> findAllWithUser() {
        return projectDao.findAllWithUser();
    }

    public List<Project> getByUser(long userId) {
        return projectDao.getByUser(userId);
    }

    public void delete(long id) {
        File file = new File(LOCATION_FOLDER + "/" + projectDao.getUserByProject(id) + "/" + id + ".xml");
        file.delete();
        projectDao.delete(projectDao.findById(id));
    }

    public void save(long userId, String projectName, InputStream inputStream) {
        String fileName = null;

        try {

            File directory = new File(LOCATION_FOLDER + "/" + userId);
            if (!directory.exists()) {
                directory.mkdir();
            }
            long projectId = projectDao.save(new Project(projectName, userDao.findById(userId)));
            fileName = LOCATION_FOLDER + "/" + userId + "/" + projectId + ".xml";

            OutputStream outpuStream = new FileOutputStream(new File(fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            outpuStream.flush();
            outpuStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
