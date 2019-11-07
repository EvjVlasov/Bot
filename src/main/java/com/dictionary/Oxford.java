package com.dictionary;

import com.dictionary.commands.Commands;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


public class Oxford {


    public static String getOxford(String message, Model model, Commands command) {
        String resultURL = dictionaryEntries(message, command);
        return executeCommand(resultURL, model, command);
    }

    private static String dictionaryEntries(String message, Commands command) {
        final String language = "en-gb";
        final String fields = command.getDescription().substring(1);
        final String strictMatch = "false";
        final String word_id = message.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;

    }


    private static String executeCommand(String resultURL, Model model, Commands command) {
        LoadProperties loadProperties = new LoadProperties();
        final String app_id = loadProperties.getProp().getProperty("OxfordAppId");
        final String app_key = loadProperties.getProp().getProperty("OxfordAppKey");

        try {
            URL url = new URL(resultURL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("app_id", app_id);
            urlConnection.setRequestProperty("app_key", app_key);

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return command.getCommand().Execute(stringBuilder.toString(), model);

        } catch (Exception e) {
            e.printStackTrace();
            return "Try another word.";
        }
    }

}