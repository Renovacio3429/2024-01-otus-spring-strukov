package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvQuestionDaoTest {

    private QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        var testFileNameProvider = new AppProperties("questions.csv");
        questionDao = new CsvQuestionDao(testFileNameProvider);
    }

    @Test
    public void read() {

        var question = getTestcaseQuestion();
        var testQuestion = questionDao.findAll().get(0);

        assertEquals(testQuestion.text(), question.text());

        for (int i = 0; i < testQuestion.answers().size(); i++) {
            assertEquals(testQuestion.answers().get(i).isCorrect(), question.answers().get(i).isCorrect());
            assertEquals(testQuestion.answers().get(i).text(), question.answers().get(i).text());
        }
    }

    private Question getTestcaseQuestion() {
        var answers = new ArrayList<Answer>();

        answers.add(new Answer("Science doesn't know this yet", true));
        answers.add(new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false));
        answers.add(new Answer("Absolutely not", false));

        return new Question("Is there life on Mars?", answers);
    }
}
