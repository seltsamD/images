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

//TODO: Change to ejb.Singleton type, we will have only 1 configuration entry
@Stateless
public class ConfigService {

    //TODO: create separate class for holding property keys;
    //TODO: let key format will be standard property file key format
    //storage.base.path=C:/tmp


    @Inject
    private ConfigDao configDAO;

    //TODO: add init stage for loading property file, something like this
    /*@PostConstruct
    private void init() {
        this.properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("file.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    //TODO: now next method could look like
    /*public String get(String key) {
        return Optional.ofNullable(configDAO.getByKey(key))
                .map(Config::getValue)
                .orElse(properties.getProperty(key));
    }*/
    //TODO: for our case its better to use more generic methods, make it more like Map =)
    // String get(String s)/Integer getInteger(String s)/Double getDouble(String s)
    public String getRootPath() {
        String rootPath = null;
        Config config = configDAO.getByKey(Project.ROOT_PATH);

        if (config != null)
            rootPath = config.getValue();

        else {
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
