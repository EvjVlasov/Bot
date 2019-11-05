import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

public class Examples extends Command {

    final static String Name = "/examples";

    @Override
    public String Execute(String json, Model model) {

        StringBuilder exBuilder = new StringBuilder();
        exBuilder.append("Examples:" + "\n" + "\n");
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(json);
            List exArray = root.findValues("examples");

            for (int i = 0; i < exArray.size(); i++) {
                List textArray = ((ArrayNode) exArray.get(i)).findValues("text");
                for (int y = 0; y < textArray.size(); y++) {
                    exBuilder.append("- " + textArray.get(y) + "\n" + "\n");
                }
            }
            model.setExamples(exBuilder.toString());
            return model.getExamples();

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    public boolean Contains(Commands command) {
        return false;
    }
}
