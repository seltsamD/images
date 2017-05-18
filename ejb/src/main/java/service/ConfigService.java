package service;

import constants.ProjectConstants;
import dao.ConfigDao;
import model.Config;
import model.Project;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Stateless
public class ConfigService{

    @Inject
    private ConfigDao configDAO;

    public String getRootPath(){
        String rootPath = null;
        Config config = configDAO.getByKey(Project.ROOT_PATH);

        if (config != null)
            rootPath = config.getValue();

        else{
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(ProjectConstants.PROPERTIES));
                rootPath = properties.getProperty(Project.ROOT_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rootPath;
    }

    public List<Config> findAll() {
        return configDAO.findAll();
    }

    public void create(String key, String value) {
        configDAO.create(new Config(key, value));
    }
}
