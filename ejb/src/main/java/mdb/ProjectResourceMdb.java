package mdb;


import service.ProjectService;

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

    public void onMessage(Message inMessage) {
        try {
        String userId = inMessage.getStringProperty("userId");
        String projectName = inMessage.getStringProperty("projectName");
            //TODO: its better to rename it to create/generate Preview
        projectService.savePreview(userId, projectName);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
