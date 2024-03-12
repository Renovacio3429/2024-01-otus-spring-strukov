package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    private QuestionDao questionDao;

    private TestServiceImpl testService;

    @Mock
    private IOService ioService;

    @Mock
    private AppProperties appProperties;

    @BeforeEach
    void setUp() {
        questionDao = new CsvQuestionDao(appProperties);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    public void executeTestFor_test() {
        when(appProperties.getTestFileName())
                .thenReturn("questions.csv");

        when(ioService.readIntForRange(anyInt(), anyInt(), anyString()))
                .thenReturn(1);

        var testStudent = new Student("Test", "Testovich");
        var result = testService.executeTestFor(testStudent);

        Assertions.assertEquals(result.getRightAnswersCount(), 1);
    }
}
