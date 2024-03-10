package ru.otus.hw.launcher;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.TestRunnerService;

@AllArgsConstructor
@Component
public class AppLauncher implements CommandLineRunner {

    private final TestRunnerService testRunnerService;

    @Override
    public void run(String... args) {
        testRunnerService.run();
    }
}
