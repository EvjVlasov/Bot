import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Pronunciation extends Command {
    final static String Name = "/pronunciations";

    @Override
    public String Execute(String json, Model model) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            model.setPronunciations(root.findValue("audioFile").textValue());
            model.setTranscription(root.findValue("phoneticSpelling").textValue());
            return "Transcription: ["+ model.getTranscription() + "]"+"\n" + model.getPronunciations();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }

    }

    @Override
    public boolean Contains(Commands command) {
        return false;
    }
}
