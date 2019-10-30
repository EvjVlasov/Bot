
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;



public class Oxford {


    public static String getOxford(String  message, Model model, Commands command) {
        String resultURL = dictionaryEntries(message, command);
        return  doInBackground(resultURL, model, command);
    }

    private static String dictionaryEntries(String message, Commands command) {
        final String language = "en-gb";
        final String fields = command.getDescription().substring(1);
        final String strictMatch = "false";
        final String word_id = message.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;

    }


    private static String doInBackground(String resultURL, Model model, Commands command) {
        LoadProperties loadProperties = new LoadProperties();
        final String app_id = loadProperties.getProp().getProperty("OxfordAppId");
        final String app_key = loadProperties.getProp().getProperty("OxfordAppKey");

        try {
            URL url = new URL(resultURL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line+ "\n");
            }

            JSONObject object1 = new JSONObject(stringBuilder.toString());
            JSONArray results = object1.getJSONArray("results");

            String result = null;
            switch (command.getDescription().substring(1)) {
                case "pronunciations":
                    result = Pronunciations(results, model);
                    break;
                case "definitions":
                    result = Definitions(results, model);
                    break;
                case "examples":
                    result = Examples(results, model);
                    break;
            }

            return result;

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private static String Definitions(JSONArray results, Model model) {

        try {

            StringBuilder defBuilder = new StringBuilder();
            defBuilder.append("Definitions:" + "\n"+ "\n");
            for (int i = 0; i < results.length() ; i++) {
                JSONArray lexicalEntries= results.getJSONObject(i).getJSONArray("lexicalEntries");
                for (int y = 0; y < lexicalEntries.length() ; y++) {
                    JSONArray entries= lexicalEntries.getJSONObject(y).getJSONArray("entries");
                    for (int n = 0; n < entries.length() ; n++) {
                        try {
                            JSONArray senses = entries.getJSONObject(n).getJSONArray("senses");
                            for (int k = 0; k < senses.length(); k++) {
                                JSONArray defArray = senses.getJSONObject(k).getJSONArray("definitions");
                                if (!defArray.isEmpty()) {
                                    defBuilder.append("- " + defArray.get(0).toString() + "\n" + "\n");
                                }
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            model.setDefinitions(defBuilder.toString());

            return model.getDefinitions();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private static String Pronunciations(JSONArray results, Model model) {

        try {

            for (int i = 0; i < results.length() ; i++) {
                JSONArray lexicalEntries = results.getJSONObject(i).getJSONArray("lexicalEntries");
                for (int y = 0; y < lexicalEntries.length() ; y++) {
                    JSONArray pronunciations= lexicalEntries.getJSONObject(y).getJSONArray("pronunciations");
                    for (int k = 0; k < pronunciations.length() ; k++) {
                        model.setPronunciations(pronunciations.getJSONObject(k).getString("audioFile").replaceAll("_", "%5f"));
                        model.setTranscription(pronunciations.getJSONObject(k).getString("phoneticSpelling"));
                    }
                }
            }

            return "Transcription: ["+ model.getTranscription() + "]"+"\n" + model.getPronunciations();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private static String Examples(JSONArray results, Model model) {

        try {

            StringBuilder exBuilder = new StringBuilder();
            exBuilder.append("Examples:" + "\n"+ "\n");
            for (int i = 0; i < results.length() ; i++) {
                JSONArray lexicalEntries= results.getJSONObject(i).getJSONArray("lexicalEntries");
                for (int y = 0; y < lexicalEntries.length() ; y++) {
                    JSONArray entries= lexicalEntries.getJSONObject(y).getJSONArray("entries");
                    for (int n = 0; n < entries.length() ; n++) {
                        JSONArray senses = entries.getJSONObject(n).getJSONArray("senses");
                        for (int k = 0; k < senses.length() ; k++) {
                            JSONArray examples = senses.getJSONObject(k).getJSONArray("examples");
                            for (int m = 0; m < examples.length() ; m++) {
                                exBuilder.append("- "+ examples.getJSONObject(m).getString("text") + "\n"+"\n");
                            }
                        }
                    }
                }
            }

            model.setExamples(exBuilder.toString());

            return model.getExamples();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}