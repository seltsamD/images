package factory;


import service.ConfigService;
import util.PropertyKeys;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

//TODO: rename it to CDIServicesFactory
// let this factory will be used to create all our services
public class CDIProjectRepositoryFactory {

    //TODO make it private
    @Inject
    ConfigService configService;

    @Produces
    public ProjectRepositoryFactory createFactory() {
        return new ProjectRepositoryFactory(configService.get(PropertyKeys.ROOT_PATH));
    }

    //TODO: add 2 methods here, with selection of implementations acc. with flags in configuration
    // ImageProcessor provideImageProcessor(){}
    // Canvas provideCanvas(){}

}
