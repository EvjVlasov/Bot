import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bot extends TelegramLongPollingBot {
    private UserRequest userRequest = new UserRequest();

 //   private ExecutorService executorService = Executors.newCachedThreadPool();

    public Bot() {
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
        final Message message = update.getMessage();
            if (message.isCommand()) {
                Commands[] cmdArray = Commands.values();

                for (Commands command : cmdArray) {
                    if (command.getDescription().equals(message.getText()) && userRequest.getMessage().getText().isEmpty()) {
                     //   executorService.submit(new Runnable() {
                      //      @Override
                      //      public void run() {
                                sendAnswer(message, "First enter the word.");
                        //    }
                      //  });
                        break;
                    }

                    if (command.getDescription().equals(message.getText()) && !userRequest.getMessage().getText().isEmpty()) {
                        Model model = new Model();
                        sendAnswer(userRequest.getMessage(), Oxford.getOxford(userRequest.getMessage().getText(), model, command));
                        break;
                    }
                }
            } else {
                userRequest.setMessage(message);
                sendAnswer(message, "Now enter the command.");
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
