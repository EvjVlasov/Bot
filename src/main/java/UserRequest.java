import org.telegram.telegrambots.meta.api.objects.Message;

public class UserRequest {
    private Message message;
    private String command;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }


}
