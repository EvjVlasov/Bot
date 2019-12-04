package com.dictionary.commands;

public enum CommandType {

    PRONUNCIATION("/pronunciations", new Pronunciation()),
    DEFINITION("/definitions", new Definition()),
    EXAMPLES("/examples", new Examples());


    private String description;
    private AbstractCommand command;

    CommandType(String description, AbstractCommand command) {
        this.description = description;
        this.command = command;

    }

    public String getDescription() {
        return description;
    }


    public AbstractCommand getCommand() {
        return command;
    }
}
