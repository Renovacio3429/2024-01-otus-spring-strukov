package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileReader;
import java.util.List;

@RequiredArgsConstructor
@Component
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
            String prefix = "file";
            String suffix = ".tmp";
            final File tempFile = File.createTempFile(prefix, suffix);
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {

                int read;
                int defaultBufferSize = 8192;
                byte[] bytes = new byte[defaultBufferSize];
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
