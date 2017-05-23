package mdb;


import generator.PreviewGenerator;
import model.db.Project;
import model.db.User;
import org.jboss.logging.Logger;
import repository.ProjectRepository;
import repository.ProjectRepositoryFactory;
import service.ProjectService;
import service.UserService;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(name = "projectMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/projectQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ProjectResourceMdb implements MessageListener {

    @Inject
    private ProjectService projectService;

    @Inject
    private UserService userService;

    @Inject
    private ProjectRepositoryFactory servicesFactory;

    //TODO: make it private
    @Inject
    PreviewGenerator previewGenerator;

    private static final Logger LOGGER = Logger.getLogger(ProjectResourceMdb.class);

    public void onMessage(Message inMessage) {
        try {
            Project project = projectService.getById(Long.parseLong(inMessage.getStringProperty("projectId")));
            User user = userService.getById(Long.parseLong(inMessage.getStringProperty("userId")));
            ProjectRepository projectRepository = servicesFactory.create(user, project);
            previewGenerator.generate(projectRepository);
        } catch (JMSException e) {
            LOGGER.error("Error at process of async generate preview" + e.getMessage());
        }


    }
}
