package com.dictionary.commands;

public enum Commands {

    PRONUNCIATION("/pronunciations", new Pronunciation()),
    DEFINITION("/definitions", new Definition()),
    EXAMPLES("/examples", new Examples());


    private String description;
    private Command command;

    Commands(String description, Command command) {
        this.description = description;
        this.command = command;

    }

    public String getDescription() {
        return description;
    }


    public Command getCommand() {
        return command;
    }
}
