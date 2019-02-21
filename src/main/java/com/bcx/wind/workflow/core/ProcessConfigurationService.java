package com.bcx.wind.workflow.core;


import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ProcessConfig;

import java.util.List;

/**
 * 流程配置模块
 *
 * @author zhanglei
 */
public interface ProcessConfigurationService {

    List<ProcessConfig> queryList(QueryFilter filter);


    int insert(ProcessConfig processConfig);


    int update(ProcessConfig processConfig);


    int deleteById(String id);


    ProcessConfig  getProcessConfigById(String id);

}



