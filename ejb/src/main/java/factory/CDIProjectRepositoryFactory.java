package factory;


import util.PropertyKeys;
import service.ConfigService;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class CDIProjectRepositoryFactory {

    @Inject
    ConfigService configService;

    @Produces
    public ProjectRepositoryFactory createFactory() {
        return new ProjectRepositoryFactory(configService.get(PropertyKeys.ROOT_PATH));
    }

}
