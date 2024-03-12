package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider testFileNameProvider;

    private QuestionDao questionDao;

    @BeforeEach
    public void init() {
        questionDao = new CsvQuestionDao(testFileNameProvider);
        when(testFileNameProvider.getTestFileName())
                .thenReturn("questions.csv");
    }

    @Test
    public void checkQuestionsReadFromFileIsCorrect() {
        var questions = questionDao.findAll();

        assertEquals(questions.size(), 1);
        assertEquals(questions.get(0).answers().size(), 3);
        assertEquals("Is there life on Mars?", questions.get(0).text());
    }
}
