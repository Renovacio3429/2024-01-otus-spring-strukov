package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

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
            var validAnswer = question.answers()
                    .stream()
                    .filter(Answer::isCorrect)
                    .findFirst();

            // skip question without correct answer
            validAnswer.ifPresent(answer -> {
                ioService.printLineLocalized("TestService.answer.the.question");
                question.answers().forEach(a -> ioService.printLine(a.text()));

                var userAnswer = ioService.readStringWithPromptLocalized("TestService.answer.user.input");
                var isAnswerValid = answer.text().equals(userAnswer);

                testResult.applyAnswer(question, isAnswerValid);
            });
        }
        return testResult;
    }
}
