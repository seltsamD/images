package cdi;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class CDIProjectsFactory {

    @Inject
    private ConfigurationBean configurationBean;

    @Produces
    public ProjectRepositoryFactory createFactory() {
        return new ProjectRepositoryFactory(configurationBean.getRootPath());
    }

}
