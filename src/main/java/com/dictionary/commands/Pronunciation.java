package com.dictionary.commands;

import com.dictionary.ModelAnswer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Pronunciation extends AbstractCommand {

    @Override
    public String execute(String json, ModelAnswer model) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            model.setPronunciation(root.findValue("audioFile").textValue());
            model.setTranscription(root.findValue("phoneticSpelling").textValue());
            return "Transcription: [" + model.getTranscription() + "]" + "\n" + model.getPronunciation();
        } catch (IOException e) {
            LOG.error("Exception: ", e);
            return WRONG_WORD;
        }

    }

}
