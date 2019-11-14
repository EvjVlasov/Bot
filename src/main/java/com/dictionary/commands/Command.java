package com.dictionary.commands;

public enum Command {

    PRONUNCIATION("/pronunciations", new Pronunciation()),
    DEFINITION("/definitions", new Definition()),
    EXAMPLES("/examples", new Examples());


    private String description;
    private BotCommand command;

    Command(String description, BotCommand command) {
        this.description = description;
        this.command = command;

    }

    public String getDescription() {
        return description;
    }


    public BotCommand getCommand() {
        return command;
    }
}
