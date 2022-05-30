package com.iotinall.canteen.service;

/**
 * 设备数据同步任务处理类
 **/
public interface SyncTaskService {
    /**
     * 同步后厨人员
     */
    void syncToTerminal(Integer type);

    /**
     * 同步后厨人员
     */
    void syncToTerminal(Integer optType, Long empId);

    /**
     * 全量同步后厨人员
     */
    void fullSyncToTerminal(Integer type);

    /**
     * 同步后厨人员
     */
    void syncOrgEmployeeToTerminal(Boolean full);
}
