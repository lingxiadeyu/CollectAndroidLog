package com.zhidaoauto.collectlog.service;

import com.zhidaoauto.collectlog.entity.Result;

import java.util.List;

public interface CollectLogService {
    List<String> getDevices();
    Result startCollectLogs();
    Result startCollectCrashLogs();
    Result searchKeyLog(String searchkey);
    Result isConnected(Result result);

}
