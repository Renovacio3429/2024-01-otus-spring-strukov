package ru.otus.hw.service;

import java.util.List;

public interface FileReaderService<T> {

    List<T> read(String fileName);
}
