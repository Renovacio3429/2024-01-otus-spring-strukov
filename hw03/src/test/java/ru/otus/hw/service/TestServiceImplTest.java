package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private LocalizedIOService localizedIOService;

    @Mock
    private QuestionDao questionDao;

    private TestService testService;

    @BeforeEach
    public void init() {
        when(questionDao.findAll()).thenReturn(getDummyQuestions());
        when(localizedIOService.readIntForRange(anyInt(), anyInt(), any())).thenReturn(2);
        testService = new TestServiceImpl(localizedIOService, questionDao);
    }

    @Test
    public void shouldInvokeIoServiceMethodsWithExpectedArgumentsDuringTestExecution() {
        var testStudent = new Student("Test", "Testovich");
        var testResult = testService.executeTestFor(testStudent);

        verify(localizedIOService, times(4)).printLineLocalized(any());
        assertEquals(1, testResult.getRightAnswersCount());
    }

    private List<Question> getDummyQuestions() {
        var question1 = new Question("test",
                List.of(
                        new Answer("answerN1", false),
                        new Answer("answerN2", true)
                ));

        return List.of(question1);
    }
}
