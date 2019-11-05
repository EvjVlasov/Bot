import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class Definition extends Command {

    final static String Name = "/definitions";

    @Override
    public String Execute(String json, Model model) {

        StringBuilder defBuilder = new StringBuilder();
        defBuilder.append("Definitions:" + "\n"+ "\n");
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(json);
            List defArray = root.findValues("definitions");
            for (int i = 0; i < defArray.size() ; i++) {
                defBuilder.append("- "+ defArray.get(i).toString() + "\n" + "\n");
            }
            model.setDefinitions(defBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return model.getDefinitions();
    }

    @Override
    public boolean Contains(Commands command) {
        return false;
    }
}
