package com.intercom.customerdistance;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CalculateDistanceImplTest {

    @InjectMocks
    CalculateDistanceImpl calculateDistanceImpl;

    @Captor
    private ArgumentCaptor<ILoggingEvent> loggingEventArgumentCaptor;

    @Mock
    private Appender<ILoggingEvent> loggingEventAppenderMock;

    private final String testResourcesPath = "src/test/resources/";

    @Test
    void generateEligibleCustomerFile_successFlow() {
        final String expectedEligibleLoggerMessage = "Eligible customers printed to output.txt.";
        final String expectedOpenInputFileLoggerMessage = "Input file customers.txt successfully opened.";
        final String outputFileName = "output.txt";
        final String expectedOutput = "[Customer Id: 4\tName: Ian Kehoe, Customer Id: 5\tName: Nora Dempsey, Customer Id: 6\tName: Theresa Enright, Customer Id: 8\tName: Eoin Ahearn, Customer Id: 11\tName: Richard Finnegan, Customer Id: 12\tName: Christina McArdle, Customer Id: 13\tName: Olive Ahearn, Customer Id: 15\tName: Michael Ahearn, Customer Id: 17\tName: Patricia Cahill, Customer Id: 23\tName: Eoin Gallagher, Customer Id: 24\tName: Rose Enright, Customer Id: 26\tName: Stephen McArdle, Customer Id: 29\tName: Oliver Ahearn, Customer Id: 30\tName: Nick Enright, Customer Id: 31\tName: Alan Behan, Customer Id: 39\tName: Lisa Ahearn]";
        File outputFile = new File(outputFileName);

        Logger root = (Logger) LoggerFactory.getLogger(CalculateDistanceImpl.class);
        root.addAppender(loggingEventAppenderMock);
        root.setLevel(Level.INFO);

        String resourcesPath = "src/main/resources/customerrecords/";
        calculateDistanceImpl.generateEligibleCustomerFile(resourcesPath + "customers.txt");

        verify(loggingEventAppenderMock, times(2)).doAppend(loggingEventArgumentCaptor.capture());
        assertEquals(expectedOpenInputFileLoggerMessage, loggingEventArgumentCaptor.getAllValues().get(0).getMessage());
        assertEquals(expectedEligibleLoggerMessage, loggingEventArgumentCaptor.getAllValues().get(1).getMessage());
        try {
            assertEquals(expectedOutput, Files.readAllLines(outputFile.toPath()).toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void generateEligibleCustomerFile_fileNotFound() {
        final String expectedErrorLoggerMessage = "Given file invalidFile not found in resources.";
        Logger root = (Logger) LoggerFactory.getLogger(CalculateDistanceImpl.class);
        root.addAppender(loggingEventAppenderMock);
        root.setLevel(Level.ERROR);

        calculateDistanceImpl.generateEligibleCustomerFile(testResourcesPath + "invalidFile");

        verify(loggingEventAppenderMock, times(1)).doAppend(loggingEventArgumentCaptor.capture());
        assertEquals(expectedErrorLoggerMessage, loggingEventArgumentCaptor.getValue().getMessage());
    }

    @Test
    void generateEligibleCustomerFile_invalidJson() {
        final String expectedErrorLoggerMessage = "Given file invalid_json.txt has unexpected JSON format.";
        Logger root = (Logger) LoggerFactory.getLogger(CalculateDistanceImpl.class);
        root.addAppender(loggingEventAppenderMock);
        root.setLevel(Level.ERROR);

        calculateDistanceImpl.generateEligibleCustomerFile(testResourcesPath + "invalid_json.txt");

        verify(loggingEventAppenderMock, times(1)).doAppend(loggingEventArgumentCaptor.capture());
        assertEquals(expectedErrorLoggerMessage, loggingEventArgumentCaptor.getValue().getMessage());
    }
}