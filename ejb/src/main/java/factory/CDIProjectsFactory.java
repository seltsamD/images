package factory;


import constants.PropertyKeys;
import service.ConfigService;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

//TODO: rename it to CDIProjectRepositoryFactory
public class CDIProjectsFactory {

    @Inject
    ConfigService configService;

    @Produces
    public CDIProjectRepositoryFactory createFactory() {
        return new CDIProjectRepositoryFactory(configService.get(PropertyKeys.ROOT_PATH));
    }

}
