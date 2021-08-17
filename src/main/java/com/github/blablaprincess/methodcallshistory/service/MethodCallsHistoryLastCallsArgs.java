package com.github.blablaprincess.methodcallshistory.service;

import com.github.blablaprincess.methodcallshistory.common.datetime.DateTimeFormats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodCallsHistoryLastCallsArgs {

    @DateTimeFormat(pattern = DateTimeFormats.YEAR_TO_MINUTE)
    private LocalDateTime timestampAfter;

    @DateTimeFormat(pattern = DateTimeFormats.YEAR_TO_MINUTE)
    private LocalDateTime timestampBefore;

}
