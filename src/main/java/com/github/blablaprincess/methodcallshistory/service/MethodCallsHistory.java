package com.github.blablaprincess.methodcallshistory.service;

import com.github.blablaprincess.methodcallshistory.repository.MethodCallDescriptionEntity;

import java.util.List;

public interface MethodCallsHistory {
    void saveCall(MethodCallDescriptionEntity call);
    List<MethodCallDescriptionEntity> getLastCalls(MethodCallsHistoryLastCallsArgs args);
}
