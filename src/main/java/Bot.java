import com.dictionary.LoadProperties;
import com.dictionary.Model;
import com.dictionary.Oxford;
import com.dictionary.commands.Commands;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;
import java.util.Optional;


public class Bot extends TelegramLongPollingSessionBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update, Optional<Session> optional) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Session session = optional.orElse(null);
            if (message.isCommand()) {
                Commands[] cmdArray = Commands.values();

                for (Commands command : cmdArray) {
                    assert session != null;
                    if (command.getDescription().equals(message.getText()) && session.getAttribute("message") == null) {
                        sendAnswer(message, "First enter the word.");
                        break;
                    }

                    if (command.getDescription().equals(message.getText()) && session.getAttribute("message") != null) {
                        Model model = new Model();
                        sendAnswer((Message) session.getAttribute("message"), Oxford.getOxford(((Message) session.getAttribute("message")).getText(), model, command));
                        break;
                    }
                }
            } else {
                assert session != null;
                session.setAttribute("message", message);
                sendAnswer(message, "Now enter the command.");
            }
        }
    }

    @Override
    public String getBotUsername() {
        LoadProperties loadProperties = new LoadProperties();
        return loadProperties.getProp().getProperty("TelegramBotName");
    }

    @Override
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
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendAudio sendAudio = new SendAudio();
            sendAudio.setChatId(message.getChatId().toString());
            sendAudio.setReplyToMessageId(message.getMessageId());
            sendAudio.setCaption(text.substring(0, text.indexOf("\n")));
            sendAudio.setAudio(text.substring(text.indexOf("\n") + 1));

            try {
                execute(sendAudio);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

}
