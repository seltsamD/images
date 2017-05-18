package cdi;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

//TODO: move factory to factory package inside ejb module
//TODO: let names of factories for CDI container starts with Cdi prefix
public class CDIProjectsFactory {

    @Inject
    private ConfigurationBean configurationBean;

    @Produces
    public ProjectRepositoryFactory createFactory() {
        return new ProjectRepositoryFactory(configurationBean.getRootPath());
    }

}
