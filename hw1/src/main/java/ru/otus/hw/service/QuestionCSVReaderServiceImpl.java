package ru.otus.hw.service;

import com.opencsv.bean.CsvToBeanBuilder;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class QuestionCSVReaderServiceImpl implements FileReaderService<Question> {

    private final FileReaderUtilsService fileReaderUtilsService;

    public QuestionCSVReaderServiceImpl(FileReaderUtilsService fileReaderUtilsService) {
        this.fileReaderUtilsService = fileReaderUtilsService;
    }

    @Override
    public List<Question> read(String fileName) {
        var file = fileReaderUtilsService.getFileFromResource(fileName);

        try (var fileReader = new FileReader(file)) {
            var csvContext = new CsvToBeanBuilder<QuestionDto>(fileReader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();

            return csvContext
                    .stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (IOException e) {
            throw new QuestionReadException("Something went wrong: ", e);
        }
    }
}
