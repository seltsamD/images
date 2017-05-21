package service;

import dao.ConfigDao;
import model.Config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Singleton
public class ConfigService {

    @Inject
    private ConfigDao configDAO;

    private Properties properties;

    @PostConstruct
    private void init() {
        this.properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("../file.properties"));
        } catch (IOException e) {
            //TODO: add JBOSS logging system for log exceptions
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return Optional.ofNullable(configDAO.getByKey(key))
                .map(Config::getValue)
                .orElse(properties.getProperty(key));
    }

    public List<Config> findAll() {
        return configDAO.findAll();
    }

    public void create(String key, String value) {
        //TODO: remove next line, in our case property is like fallback default settings,
        //we don`t need to save there some data
        properties.put(key, value);
        configDAO.create(new Config(key, value));
    }
}
