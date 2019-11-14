package com.dictionary.commands;

import com.dictionary.ModelAnswer;

public interface BotCommand {
    String WRONG_WORD = "Try another word.";

    String execute(String json, ModelAnswer model);

}
