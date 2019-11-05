public abstract class Command {

    public abstract String Execute(String json, Model model);

    public abstract boolean Contains(Commands command);
}
