package com.HotelApp.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationShutdownHookTest {
    private static final String KEY_FILE = "secret.key";

    @Mock
    private ApplicationShutdownHook applicationShutdownHook;


    @Test
    void testOnShutdown_fileExists() throws Exception {
        Path path = Paths.get(KEY_FILE);
        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
            filesMock.when(() -> Files.deleteIfExists(path)).thenReturn(true);

            try (MockedStatic<LoggerFactory> loggerFactoryMock = Mockito.mockStatic(LoggerFactory.class)) {
                Logger mockLogger = mock(Logger.class);
                loggerFactoryMock.when(() -> LoggerFactory.getLogger(ApplicationShutdownHook.class)).thenReturn(mockLogger);

                applicationShutdownHook.onShutdown();

                filesMock.verify(() -> Files.deleteIfExists(path));
                verify(mockLogger).info(() -> "file deleted hooOK");
            }
        }
    }

    @Test
    void testOnShutdown_fileDoesNotExist() throws Exception {
        Path path = Paths.get(KEY_FILE);
        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
            filesMock.when(() -> Files.deleteIfExists(path)).thenReturn(false);

            try (MockedStatic<LoggerFactory> loggerFactoryMock = Mockito.mockStatic(LoggerFactory.class)) {
                Logger mockLogger = mock(Logger.class);
                loggerFactoryMock.when(() -> LoggerFactory.getLogger(ApplicationShutdownHook.class)).thenReturn(mockLogger);

                applicationShutdownHook.onShutdown();

                filesMock.verify(() -> Files.deleteIfExists(path));
                verify(mockLogger).info(() -> "file deleted hooOK");
            }
        }
    }

    @Test
    void testOnShutdown_exceptionThrown() {
        Path path = Paths.get(KEY_FILE);
        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
            filesMock.when(() -> Files.deleteIfExists(path)).thenThrow(new RuntimeException("Test Exception"));

            try (MockedStatic<LoggerFactory> loggerFactoryMock = Mockito.mockStatic(LoggerFactory.class)) {
                Logger mockLogger = mock(Logger.class);
                loggerFactoryMock.when(() -> LoggerFactory.getLogger(ApplicationShutdownHook.class)).thenReturn(mockLogger);

                assertDoesNotThrow(() -> applicationShutdownHook.onShutdown());

                filesMock.verify(() -> Files.deleteIfExists(path));
                verify(mockLogger, never()).info(() -> "file deleted hooOK");
            }
        }
    }
}