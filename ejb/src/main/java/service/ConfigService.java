package service;

import dao.ConfigDao;
import model.db.Config;

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
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return Optional.ofNullable(configDAO.getByKey(key))
                .map(Config::getValue)
                .orElse(properties.getProperty(key));
    }

    public boolean getBoolean(String key){

       String result = Optional.ofNullable(configDAO.getByKey(key))
                .map(Config::getValue)
                .orElse(properties.getProperty(key));
        return Boolean.parseBoolean(result);
    }

    public List<Config> findAll() {
        return configDAO.findAll();
    }

}
