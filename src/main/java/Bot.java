import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Bot extends TelegramLongPollingBot {
    private UserRequest userRequest = new UserRequest();

    public Bot() {
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException var3) {
            var3.printStackTrace();
        }

    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            if (message.getText().substring(0, 1).equals("/")) {
                Commands[] var3 = Commands.values();
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Commands command = var3[var5];
                    if (command.getDescription().equals(message.getText()) && this.userRequest.getMessage().getText().isEmpty()) {
                        this.sendAnswer(message, "First enter the word.");
                        break;
                    }

                    if (command.getDescription().equals(message.getText()) && !this.userRequest.getMessage().getText().isEmpty()) {
                        Model model = new Model();
                        this.sendAnswer(this.userRequest.getMessage(), Oxford.getOxford(this.userRequest.getMessage().getText(), model, command));
                        break;
                    }
                }
            } else {
                this.userRequest.setMessage(message);
                this.sendAnswer(message, "Now enter the command.");
            }
        }

    }

    public String getBotUsername() {
        LoadProperties loadProperties = new LoadProperties();
        return loadProperties.getProp().getProperty("TelegramBotName");
    }

    public String getBotToken() {
        LoadProperties loadProperties = new LoadProperties();
        return loadProperties.getProp().getProperty("TelegramBotToken");
    }

    public void sendAnswer(Message message, String text) {
        if (!text.substring(text.length() - 4).equals(".mp3")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setReplyToMessageId(message.getMessageId());
            sendMessage.setText(text);

            try {
                this.execute(sendMessage);
            } catch (TelegramApiException var6) {
                var6.printStackTrace();
            }
        } else {
            SendAudio sendAudio = new SendAudio();
            sendAudio.setChatId(message.getChatId().toString());
            sendAudio.setReplyToMessageId(message.getMessageId());
            sendAudio.setCaption(text.substring(0, text.indexOf("\n")));
            sendAudio.setAudio(text.substring(text.indexOf("\n") + 1));

            try {
                this.execute(sendAudio);
            } catch (TelegramApiException var5) {
                var5.printStackTrace();
            }
        }

    }
}
