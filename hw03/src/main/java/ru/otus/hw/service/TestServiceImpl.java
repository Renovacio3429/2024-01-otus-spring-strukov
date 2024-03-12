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

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLineLocalized("TestService.answer.the.question");
            ioService.printLine(question.text());
            ioService.printLineLocalized("TestService.answer.the.answers");

            var answerIdxMapping = this.printAnswers(question);
            ioService.printLineLocalized("TestService.answer.user.input");
            var userAnswer = ioService.readIntForRange(
                    1, 
                    question.answers().size(),
                    ioService.getMessage("TestService.answer.the.error.number")
            );
            var isAnswerValid = answerIdxMapping.get(userAnswer).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private Map<Integer, Answer> printAnswers(Question question) {
        var answerIdxMapping = new HashMap<Integer, Answer>();

        for (int i = 0; i < question.answers().size(); i++) {
            var answer = question.answers().get(i);

            var mapIdx = i + 1;
            ioService.printLine(mapIdx + ") " + answer.text());
            answerIdxMapping.put(mapIdx, answer);
        }

        return answerIdxMapping;
    }
}
