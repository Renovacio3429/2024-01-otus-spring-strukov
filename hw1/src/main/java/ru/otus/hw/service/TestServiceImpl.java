package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;
    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        printQuestions(questionDao.findAll());
    }

    private void printQuestions(List<Question> questions) {
        questions.forEach(q -> {
            ioService.printLine("Question: " + q.text());
            ioService.printLine("Answers: ");
            q.answers().forEach(a -> ioService.printLine(a.text()));
            ioService.printLine("");
        });
    }
}
