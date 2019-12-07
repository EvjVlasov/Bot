package com.dictionary.commands;

import com.dictionary.ModelAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PronunciationTest {
    private ModelAnswer model;
    private String json;
    private AbstractCommand command;
    private static final String FILE_NAME = "src/main/resources/test/TestPronunciation.JSON";

    @BeforeEach
    void setUp() {
        model = new ModelAnswer();

        List<String> lines = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            lines = Files.readAllLines(Paths.get(FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : lines) {
            stringBuilder.append(line).append("\n");
        }
        json = stringBuilder.toString();

        command = CommandType.PRONUNCIATION.getCommand();

    }

    @Test
    void shouldExecute() {
        command.execute(json, model);
        assertEquals(".mp3", model.getPronunciation().substring(model.getPronunciation().length() - 4));
        assertEquals("kat", model.getTranscription());
    }
}