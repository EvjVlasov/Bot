package com.dictionary.commands;

import com.dictionary.ModelAnswer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Pronunciation implements BotCommand {
    private static final Logger log = LogManager.getLogger(Pronunciation.class);

    @Override
    public String execute(String json, ModelAnswer model) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            model.setPronunciation(root.findValue("audioFile").textValue());
            model.setTranscription(root.findValue("phoneticSpelling").textValue());
            return "Transcription: [" + model.getTranscription() + "]" + "\n" + model.getPronunciation();
        } catch (IOException e) {
            log.error("Exception: ", e);
            return WRONG_WORD;
        }

    }

}
