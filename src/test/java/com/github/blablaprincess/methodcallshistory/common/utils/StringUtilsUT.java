package com.github.blablaprincess.methodcallshistory.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class StringUtilsUT {

    private final StringUtils stringUtils = new StringUtils();

    @DisplayName("cropByMaxLength()")
    @ParameterizedTest(name = "{0}")
    @MethodSource("getCropByMaxLengthCases")
    void cropByMaxLength(String description, String input, int maxLength, String expected) {
        // Act
        String result = stringUtils.cropByMaxLength(input, maxLength);

        // Assert
        assertEquals(expected, result);
    }

    private static Stream<Arguments> getCropByMaxLengthCases() {

        return Stream.of(
                arguments("input < maxLength",               "123456",     7, "123456"),
                arguments("input == maxLength",              "1234567",    7, "1234567"),
                arguments("input (even) > maxLength (odd)",  "12345678",   7, "123…678"),
                arguments("input (odd)  > maxLength (odd)",  "123456789",  7, "123…789"),
                arguments("input (odd)  > maxLength (even)", "123456789",  8, "123…6789"),
                arguments("input (even) > maxLength (even)", "1234567890", 8, "123…7890")
        );
    }

}