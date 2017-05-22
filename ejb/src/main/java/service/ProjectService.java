package service;

import dao.ProjectDao;
import dao.UserDao;
import factory.CDIProjectRepositoryFactory;
import generator.PreviewGenerator;
import model.db.Project;
import model.db.User;
import org.apache.commons.io.FilenameUtils;
import repository.ProjectRepository;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
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
    private UserDao userDao;

    @Inject
    private CDIProjectRepositoryFactory projectsFactory;

    //TODO; read, comments inside PreviewGenerator, use Inject here
    PreviewGenerator previewGenerator;

    @Inject ConfigService configService;


    public List<Project> findAll() {
        return projectDao.findAll();
    }

    public List<Project> getByUserName(String username) {
        return projectDao.getByUserName(username);
    }

    public void delete(long id) {
        Project project = projectDao.findById(id);
        projectsFactory.createFactory().create(project.getUser(), project).deleteProject();
        projectDao.delete(project);
    }


    public void save(long userId, String fileName, InputStream inputStream) {
        Project project = projectDao.save(FilenameUtils.getBaseName(fileName), userDao.findById(userId));
        User user = userDao.findById(userId);
        ProjectRepository projectRepository = projectsFactory.createFactory().create(user, project);
        projectRepository.unzipProject(inputStream);
        previewGenerator = new PreviewGenerator(projectRepository, configService);
        previewGenerator.prepareImages();
        previewGenerator.generate();

    }


    public byte[] getPreviewBody(String username, String projectName) {
        User user = userDao.findByUsername(username);
        Project project = projectDao.getByProjectName(projectName);
        File file = projectsFactory.createFactory().create(user, project).callPreview();
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

    public boolean isUniqueName(String projectName){
        return projectDao.isUniqueName(projectName);
    }

}
