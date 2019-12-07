package com.dictionary;

import com.dictionary.commands.CommandType;
import com.dictionary.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class OxfordDictionary {
    private static final String WRONG_WORD = "Try another word.";
    private static final Logger LOG = LogManager.getLogger(OxfordDictionary.class);

    public String getOxfordDictionary(String message, ModelAnswer model, CommandType command) {
        String resultURL = dictionaryEntries(message, command);
        try {
            return executeCommand(resultURL, model, command);
        } catch (IOException e) {
            LOG.error("Exception: ", e);
            return WRONG_WORD;
        }
    }

    private String dictionaryEntries(String message, CommandType command) {
        final String baseURL = "https://od-api.oxforddictionaries.com:443/api/v2/entries/";
        final String language = "en-gb";
        final String fields = command.getDescription().substring(1);
        final String strictMatch = "false";
        final String wordId = message.toLowerCase();
        return baseURL + language + "/" + wordId + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;

    }


    private String executeCommand(String resultURL, ModelAnswer model, CommandType command) throws IOException {
        PropertiesLoader loadProperties = new PropertiesLoader();
        final String appId = loadProperties.getProp().getProperty("OxfordAppId");
        final String appKey = loadProperties.getProp().getProperty("OxfordAppKey");

        URL url = new URL(resultURL);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestProperty("app_id", appId);
        urlConnection.setRequestProperty("app_key", appKey);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return command.getCommand().execute(stringBuilder.toString(), model);

        } catch (Exception e) {
            LOG.error("Exception: ", e);
            return WRONG_WORD;
        }
    }

}