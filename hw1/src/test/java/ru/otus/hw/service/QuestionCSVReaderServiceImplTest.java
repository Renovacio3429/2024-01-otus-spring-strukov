package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionCSVReaderServiceImplTest {

    private final String TEST_FILE_NAME = "questions.csv";

    @Mock
    private FileReaderUtilsService fileReaderUtilsService;

    private FileReaderService<Question> fileReaderService;

    @BeforeEach
    void setUp() {
        fileReaderUtilsService = new FileReaderUtilsServiceImpl();
        fileReaderService = new QuestionCSVReaderServiceImpl(fileReaderUtilsService);
    }

    @Test
    public void read() {

        var question = getTestcaseQuestion();
        var testQuestions = fileReaderService.read(TEST_FILE_NAME);
        var testQuestion = testQuestions.get(0);

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
