package com.bcx.wind.workflow.flowManager;

import com.bcx.wind.workflow.core.pojo.TransferResult;

public interface Manager {

    /**
     * 转办管理
     *
     * @return            转办结果
     */
    TransferResult transfer();
}
