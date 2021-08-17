package com.github.blablaprincess.methodcallshistory.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blablaprincess.methodcallshistory.repository.MethodCallDescriptionEntity;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Execution(ExecutionMode.SAME_THREAD)
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DBUnit(cacheConnection = false, leakHunter = true, caseInsensitiveStrategy = LOWERCASE)
@RequiredArgsConstructor
@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
class MethodCallsHistoryMethodSpringPgSqlContainerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private final String presetDs = "preset/MethodCallsHistoryController/";
    private final String expectedDs = "expected/MethodCallsHistoryController/";

    @Test
    @DisplayName("GET / (mapping test)")
    @DataSet(presetDs + "GetMethodCallDescriptionEntityPDS.yml")
    void getMethodCallDescriptionEntity() throws Exception {
        // Arrange
        MethodCallDescriptionEntity entity =
                MethodCallDescriptionEntity.builder()
                        .id(UUID.fromString("10000000-1000-1000-9000-900000000000"))
                        .method("method_0")
                        .args("1, 2, 3")
                        .successful(true)
                        .response("response")
                        .timestamp(LocalDateTime.parse("2010-01-01T00:00"))
                        .build();
        List<MethodCallDescriptionEntity> expected = asList(entity);


        // Act
        String responseBody = mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<MethodCallDescriptionEntity> response =
                mapper.readValue(responseBody, new TypeReference<List<MethodCallDescriptionEntity>>() {
                });

        // Assert
        assertEquals(expected, response);
    }

    @DisplayName("GET /")
    @ParameterizedTest(name = "{0}")
    @MethodSource("getCallsCases")
    @DataSet(presetDs + "GetMethodCallsPDS.yml")
    void getMethodCalls(String url, List<String> expected) throws Exception {
        // Act
        String responseBody = mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<MethodCallDescriptionEntity> response =
                mapper.readValue(responseBody, new TypeReference<List<MethodCallDescriptionEntity>>() {
                });

        List<String> responseValues = response.stream()
                .map(MethodCallDescriptionEntity::getResponse)
                .collect(Collectors.toList());

        // Assert
        assertEquals(expected, responseValues);
    }

    @Test
    @DisplayName("POST /save")
    @DataSet(presetDs + "SaveMethodCallDescriptionEntityPDS.yml")
    @ExpectedDataSet(expectedDs + "SaveMethodCallDescriptionEntityEDS.yml")
    void save() throws Exception {
        // Arrange
        String method = "method_0";
        String args = "1, 2, 3, 4";
        String response = "answer";
        String timestamp = "2010-01-01 00:00";
        String url = String.format("/save?method=%s&args=%s&response=%s&successful=%s&timestamp=%s",
                method, args, response, true, timestamp);

        // Act
        mvc.perform(MockMvcRequestBuilders.post(url))
                .andExpect(status().isOk());
    }

    static Stream<Arguments> getCallsCases() {
        return Stream.of(
                arguments(url(),
                        asList("response 0", "response 1")),
                arguments(url(after("00:30")),
                        asList("response 1")),
                arguments(url(before("00:30")),
                        asList("response 0")),
                arguments(url(after("00:00"), before("01:00")),
                        asList("response 0", "response 1")),
                arguments(url(after("01:00"), before("00:00")),
                        asList())
        );
    }

    private static String after(String time) {
        return "timestampAfter=2021-01-01 " + time;
    }

    private static String before(String time) {
        return "timestampBefore=2021-01-01 " + time;
    }

    private static String url(String... params) {
        return "/?" + String.join("&", params);
    }

}