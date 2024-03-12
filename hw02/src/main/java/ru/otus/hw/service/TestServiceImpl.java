package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine("Question: " + question.text());
            ioService.printLine("Possible answers:" + System.lineSeparator());

            var answerIdxMapping = this.printAnswers(question);
            var userAnswer = ioService.readIntForRange(1, question.answers().size(), "Write correct answer number!");
            var isAnswerValid = answerIdxMapping.get(userAnswer).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private Map<Integer, Answer> printAnswers(Question question) {
        var answerIdxMapping = new HashMap<Integer, Answer>();
        IntStream.rangeClosed(1, question.answers().size())
                .forEach(i -> {
                    var answer = question.answers().get(i - 1);
                    ioService.printLine(i + ") " + answer.text());
                    answerIdxMapping.put(i, answer);
                });

        return answerIdxMapping;
    }
}
