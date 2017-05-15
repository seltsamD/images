package service.Impl;

import dao.ConfigDao;
import model.Config;
import service.ConfigService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ConfigServiceImpl implements ConfigService{

    @Inject
    private ConfigDao configDAO;

    @Override
    public List<Config> findAll() {
        return configDAO.findAll(Config.class);
    }

    @Override
    public void create(String key, String value) {
        configDAO.create(new Config(key, value));
    }
}
