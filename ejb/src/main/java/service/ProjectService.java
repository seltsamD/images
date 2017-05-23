package service;

import dao.ProjectDao;
import dao.UserDao;
import factory.CDIServicesFactory;
import generator.PreviewGenerator;
import model.db.Project;
import model.db.User;
import org.apache.commons.io.FilenameUtils;
import repository.ProjectRepository;
import repository.ProjectRepositoryFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.jms.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;


@Stateless
public class ProjectService {

    @Inject
    private ProjectDao projectDao;

    @Inject
    UserDao userDao;


    @Inject
    private ProjectRepositoryFactory servicesFactory;

    @Inject PreviewGenerator previewGenerator;

    @Inject
    ConfigService configService;


    public List<Project> findAll() {
        return projectDao.findAll();
    }

    public List<Project> getByUserName(String username) {
        return projectDao.getByUserName(username);
    }

    public void delete(long id) {
        Project project = projectDao.findById(id);
        servicesFactory.create(project.getUser(), project).deleteProject();
        projectDao.delete(project);
    }


    public void save(long userId, String fileName, InputStream inputStream) {
        Project project = projectDao.save(FilenameUtils.getBaseName(fileName), userDao.findById(userId));
        User user = userDao.findById(userId);
        ProjectRepository projectRepository = servicesFactory.create(user, project);
        projectRepository.unzipProject(inputStream);
        previewGenerator.generate(projectRepository);

    }


    public byte[] getPreviewBody(String username, String projectName) {
        User user = userDao.findByUsername(username);
        Project project = projectDao.getByProjectName(projectName);
        File file = servicesFactory.create(user, project).getPreview();
        if (file == null || !file.exists())
            return null;

        byte[] imageData = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(file);
            ImageIO.write(image, "png", baos);
            imageData = Base64.getEncoder().encode(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageData;
    }

    public boolean isUniqueName(String projectName) {
        return projectDao.isUniqueName(projectName);
    }

}
