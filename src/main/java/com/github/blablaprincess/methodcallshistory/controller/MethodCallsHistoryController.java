package com.github.blablaprincess.methodcallshistory.controller;

import com.github.blablaprincess.methodcallshistory.common.datetime.DateTimeFormats;
import com.github.blablaprincess.methodcallshistory.repository.MethodCallDescriptionEntity;
import com.github.blablaprincess.methodcallshistory.service.MethodCallsHistoryLastCallsArgs;
import com.github.blablaprincess.methodcallshistory.service.MethodCallsHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MethodCallsHistoryController {

    private final MethodCallsHistoryService methodCallsHistoryService;

    @GetMapping("")
    public List<MethodCallDescriptionEntity> getMethodCalls(MethodCallsHistoryLastCallsArgs args) {
        return methodCallsHistoryService.getLastCalls(args);
    }

    @PostMapping("/save")
    public void saveMethodCall(String args,
                               String response,
                               @RequestParam String method,
                               @RequestParam Boolean successful,
                               @RequestParam @DateTimeFormat(pattern = DateTimeFormats.YEAR_TO_MINUTE) LocalDateTime timestamp) {
        methodCallsHistoryService.saveCall(MethodCallDescriptionEntity.builder()
                .args(args)
                .response(response)
                .method(method)
                .successful(successful)
                .timestamp(timestamp)
                .build());
    }

}
