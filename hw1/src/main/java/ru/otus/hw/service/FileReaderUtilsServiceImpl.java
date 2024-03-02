package ru.otus.hw.service;

import ru.otus.hw.Application;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class FileReaderUtilsServiceImpl implements FileReaderUtilsService {

    public File getFileFromResource(String fileName) {

        ClassLoader classLoader = Application.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("File not found! " + fileName);
        }

        try {
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
