package com.dictionary.commands;

import com.dictionary.ModelAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class AbstractCommand {
    static final String WRONG_WORD = "Try another word.";
    static final Logger LOG = LogManager.getLogger(AbstractCommand.class);

    public abstract String execute(String json, ModelAnswer model);

}
