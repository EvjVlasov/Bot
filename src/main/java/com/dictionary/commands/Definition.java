package com.dictionary.commands;

import com.dictionary.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

public class Definition extends Command {

    @Override
    public String Execute(String json, Model model) {

        StringBuilder defBuilder = new StringBuilder();
        defBuilder.append("Definitions:" + "\n" + "\n");
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(json);
            List defArray = root.findValues("definitions");
            for (Object o : defArray) {
                defBuilder.append(o.toString()).append("\n").append("\n");
            }
            model.setDefinitions(defBuilder.toString());
            return model.getDefinitions();
        } catch (IOException e) {
            e.printStackTrace();
            return "Try another word.";
        }

    }

}
