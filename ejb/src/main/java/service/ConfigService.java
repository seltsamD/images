package service;

import dao.ConfigDao;
import model.Config;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ConfigService{

    @Inject
    private ConfigDao configDAO;

    public List<Config> findAll() {
        return configDAO.findAll();
    }

    public void create(String key, String value) {
        configDAO.create(new Config(key, value));
    }
}
