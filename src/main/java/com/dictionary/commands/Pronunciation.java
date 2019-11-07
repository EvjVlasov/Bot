package com.dictionary.commands;

import com.dictionary.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Pronunciation extends Command {

    @Override
    public String Execute(String json, Model model) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            model.setPronunciation(root.findValue("audioFile").textValue());
            model.setTranscription(root.findValue("phoneticSpelling").textValue());
            return "Transcription: ["+ model.getTranscription() + "]"+"\n" + model.getPronunciation();
        } catch (IOException e) {
            e.printStackTrace();
            return "Try another word.";
        }

    }

}
