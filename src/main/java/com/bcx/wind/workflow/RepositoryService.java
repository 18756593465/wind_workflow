package com.bcx.wind.workflow;

import com.bcx.wind.workflow.core.ProcessConfigurationService;
import com.bcx.wind.workflow.core.ProcessService;

/**
 * 静态资源模块
 *
 * @author zhanglei
 */
public interface RepositoryService {

    /**
     * 流程定义模块
     * @return   ProcessService
     */
    ProcessService processService();

    /**
     * 流程配置模块
     * @return   ProcessConfigurationService
     */
    ProcessConfigurationService processConfigurationService();






}
