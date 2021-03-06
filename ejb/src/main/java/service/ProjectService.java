package service;

import canvas.CanvasBFO;
import dao.ProjectDao;
import dao.UserDao;
import model.db.Project;
import model.db.User;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;
import repository.ProjectRepository;
import repository.ProjectRepositoryFactory;
import util.ProjectUtils;

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
    private UserDao userDao;


    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/jms/queue/projectQueue")
    private Queue queue;

    @Inject
    private ProjectRepositoryFactory servicesFactory;

    private static final Logger LOGGER = Logger.getLogger(CanvasBFO.class);

    @Inject
    private ConfigService configService;


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

    public Project getById(long id) {
        return projectDao.findById(id);
    }


    public void save(long userId, String fileName, InputStream inputStream) {
        Project project = projectDao.save(FilenameUtils.getBaseName(fileName), userDao.findById(userId));
        User user = userDao.findById(userId);
        ProjectRepository projectRepository = servicesFactory.create(user, project);
        projectRepository.unzipProject(inputStream);
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(queue);
            TextMessage textMessage = session.createTextMessage();
            textMessage.setObjectProperty("userId", userId);
            textMessage.setStringProperty("projectId", String.valueOf(project.getId()));
            messageProducer.send(textMessage);
        } catch (JMSException e) {
            LOGGER.error("Error at process of send message " + e.getMessage());
        }


    }


    public byte[] getPreviewBody(String username, String projectName) {
        User user = userDao.findByUsername(username);
        Project project = projectDao.getByProjectName(projectName);
        File file = servicesFactory.create(user, project).getPreview(ProjectUtils.PREVIEW_TYPES.PNG);
        if (file == null || !file.exists())
            return null;
        return ProjectUtils.prepareImage(file);
    }


}
