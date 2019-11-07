package com.dictionary.commands;

import com.dictionary.Model;

public abstract class Command {

    public abstract String Execute(String json, Model model);

}
