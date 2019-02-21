package com.bcx.wind.workflow;

import com.bcx.wind.workflow.core.ProcessConfigurationService;
import com.bcx.wind.workflow.core.ProcessService;

public class RepositoryServiceImpl implements RepositoryService {

    private ProcessService processService;
    private ProcessConfigurationService processConfigurationService;

    public RepositoryServiceImpl(ProcessService processService,
            ProcessConfigurationService processConfigurationService){
        super();

        this.processConfigurationService = processConfigurationService;
        this.processService = processService;
    }


    @Override
    public ProcessService processService() {
        return processService;
    }

    @Override
    public ProcessConfigurationService processConfigurationService() {
        return processConfigurationService;
    }

}
