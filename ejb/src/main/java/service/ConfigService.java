package service;

import model.Config;

import javax.ejb.Remote;
import java.util.List;


@Remote
public interface ConfigService {

    List<Config> findAll();

    void create(String key, String value);
}
