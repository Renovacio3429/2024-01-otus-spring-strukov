package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            var validAnswer = question.answers()
                    .stream()
                    .filter(Answer::isCorrect)
                    .findFirst();

            // skip question without correct answer
            validAnswer.ifPresent(answer -> {
                printQuestion(question);

                var userAnswer = ioService.readStringWithPrompt("Write your answer: ");
                var isAnswerValid = answer.text().equals(userAnswer);

                testResult.applyAnswer(question, isAnswerValid);
            });
        }
        return testResult;
    }

    private void printQuestion(Question q) {
        ioService.printLine("Question: " + q.text() + "%n");
        ioService.printLine("Possible answers: ");
        ioService.printLine("");
        q.answers().forEach(a -> ioService.printLine(a.text()));
        ioService.printLine("");
    }
}
