import com.dictionary.PropertiesLoader;
import com.dictionary.ModelAnswer;
import com.dictionary.OxfordDictionary;
import com.dictionary.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger log = LogManager.getLogger(Bot.class);

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            log.error("Exception: ", e);
        }

    }

    @Override
    public void onUpdateReceived(Update update, Optional<Session> optional) {
        final String MESSAGE = "message";
        final String WORD_REQUEST = "First enter the word.";
        final String COMMAND_REQUEST = "Now enter the command.";

        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Session session = optional.orElse(null);
            if (session != null) {
                if (message.isCommand()) {
                    Command[] cmdArray = Command.values();

                    for (Command command : cmdArray) {
                        if (command.getDescription().equals(message.getText()) && session.getAttribute(MESSAGE) == null) {
                            sendAnswer(message, WORD_REQUEST);
                            break;
                        }

                        if (command.getDescription().equals(message.getText()) && session.getAttribute(MESSAGE) != null) {
                            ModelAnswer model = new ModelAnswer();
                            OxfordDictionary oxfordDictionary = new OxfordDictionary();
                            sendAnswer((Message) session.getAttribute(MESSAGE), oxfordDictionary.getOxfordDictionary(((Message) session.getAttribute(MESSAGE)).getText(), model, command));
                            break;
                        }

                    }
                } else {
                    session.setAttribute(MESSAGE, message);
                    sendAnswer(message, COMMAND_REQUEST);
                }
            } else {
                log.error("Session is null.");
            }
        }
    }

    @Override
    public String getBotUsername() {
        PropertiesLoader loadProperties = new PropertiesLoader();
        return loadProperties.getProp().getProperty("TelegramBotName");
    }

    @Override
    public String getBotToken() {
        PropertiesLoader loadProperties = new PropertiesLoader();
        return loadProperties.getProp().getProperty("TelegramBotToken");
    }

    private void sendAnswer(Message message, String text) {
        final String EXTENSION = ".mp3";

        if (!text.substring(text.length() - 4).equals(EXTENSION)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setReplyToMessageId(message.getMessageId());
            sendMessage.setText(text);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Exception: ", e);
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
                log.error("Exception: ", e);
            }
        }

    }

}
