package com.dictionary;

import com.dictionary.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class OxfordDictionary {
    private final String WRONG_WORD = "Try another word.";
    private static final Logger log = LogManager.getLogger(OxfordDictionary.class);

    public String getOxfordDictionary(String message, ModelAnswer model, Command command) {
        String resultURL = dictionaryEntries(message, command);
        try {
            return executeCommand(resultURL, model, command);
        } catch (IOException e) {
            log.error("Exception: ", e);
            return WRONG_WORD;
        }
    }

    private String dictionaryEntries(String message, Command command) {
        final String BASE_URL = "https://od-api.oxforddictionaries.com:443/api/v2/entries/";
        final String LANGUAGE = "en-gb";
        final String FIELDS = command.getDescription().substring(1);
        final String STRICT_MATCH = "false";
        final String WORD_ID = message.toLowerCase();
        return BASE_URL + LANGUAGE + "/" + WORD_ID + "?" + "fields=" + FIELDS + "&strictMatch=" + STRICT_MATCH;

    }


    private String executeCommand(String resultURL, ModelAnswer model, Command command) throws IOException {
        PropertiesLoader loadProperties = new PropertiesLoader();
        final String APP_ID = loadProperties.getProp().getProperty("OxfordAppId");
        final String APP_KEY = loadProperties.getProp().getProperty("OxfordAppKey");

        URL url = new URL(resultURL);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestProperty("app_id", APP_ID);
        urlConnection.setRequestProperty("app_key", APP_KEY);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return command.getCommand().execute(stringBuilder.toString(), model);

        } catch (Exception e) {
            log.error("Exception: ", e);
            return WRONG_WORD;
        }
    }

}