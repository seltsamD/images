package cdi;

import service.ConfigService;

import javax.inject.Inject;
import javax.inject.Singleton;

//TODO: remove this, we have ConfigService, it`s the same
@Singleton
public class ConfigurationBean {

    @Inject
    ConfigService configService;

    public String getRootPath(){
        String rootPath = configService.getRootPath();

        return rootPath;
    }
}
