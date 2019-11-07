package com.dictionary.commands;

import com.dictionary.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

public class Examples extends Command {

    @Override
    public String Execute(String json, Model model) {

        StringBuilder exBuilder = new StringBuilder();
        exBuilder.append("Examples:" + "\n" + "\n");
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(json);
            List exArray = root.findValues("examples");

            for (Object o : exArray) {
                List textArray = ((ArrayNode) o).findValues("text");
                for (Object value : textArray) {
                    exBuilder.append(value).append("\n").append("\n");
                }
            }
            model.setExamples(exBuilder.toString());
            return model.getExamples();

        } catch (Exception e) {
            e.printStackTrace();
            return "Try another word.";
        }
    }

}
