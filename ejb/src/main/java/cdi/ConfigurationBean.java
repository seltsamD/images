package cdi;

import constants.ProjectConstants;
import dao.ConfigDao;
import model.Project;
import service.ConfigService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Singleton
public class ConfigurationBean {

    @Inject
    ConfigService configService;

    public String getRootPath(){
            String rootPath = configService.getRootPath();

            return rootPath;
    }
}
