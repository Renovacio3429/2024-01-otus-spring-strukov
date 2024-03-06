package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.*;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        return this.readFileByName(fileNameProvider.getTestFileName());
    }

    private List<Question> readFileByName(String fileName) {
        var file = this.getFileFromResources(fileName);

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

    private InputStream getISFromResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public File getFileFromResources (String fileName) {
        InputStream in = getISFromResource(fileName);

        try {
            String PREFIX = "file";
            String SUFFIX = ".tmp";
            final File tempFile = File.createTempFile(PREFIX, SUFFIX);
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {

                int read;
                int DEFAULT_BUFFER_SIZE = 8192;
                byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                return tempFile;
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
