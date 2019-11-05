import org.telegram.telegrambots.meta.api.objects.Message;

public class UserRequest {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
