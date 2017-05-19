package service;

import dao.ConfigDao;
import model.Config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;

@Singleton
public class ConfigService {

    @Inject
    private ConfigDao configDAO;

    private Properties properties;

    @PostConstruct
    private void init() {
        this.properties = new Properties();
        try {
            String path = this.getClass().getResourceAsStream("../file.properties").toString();
            properties.load(this.getClass().getResourceAsStream("../file.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key){
        return Optional.ofNullable(configDAO.getByKey(key))
                .map(Config::getValue)
                .orElse(properties.getProperty(key));
    }

    //TODO: for our case its better to use more generic methods, make it more like Map =)
    // String get(String s)/Integer getInteger(String s)/Double getDouble(String s)

    public List<Config> findAll() {
        return configDAO.findAll();
    }

    public void create(String key, String value) {
        properties.put(key, value);
        configDAO.create(new Config(key, value));
    }
}
