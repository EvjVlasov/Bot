package com.dictionary.commands;

import com.dictionary.ModelAnswer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class Definition implements BotCommand {
    private static final Logger log = LogManager.getLogger(Definition.class);

    @Override
    public String execute(String json, ModelAnswer model) {

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
            log.error("Exception: ", e);
            return WRONG_WORD;
        }

    }

}
