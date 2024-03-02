package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.FileReaderService;

import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;
    private final FileReaderService<Question> fileReaderService;

    @Override
    public List<Question> findAll() {
        return fileReaderService.read(fileNameProvider.getTestFileName());
    }
}
