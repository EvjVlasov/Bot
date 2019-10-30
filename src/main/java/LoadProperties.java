import java.io.FileInputStream;
import java.util.Properties;

public class LoadProperties {
    private Properties prop = new Properties();

    public LoadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/Bot.properties")) {
            prop.load(fileInputStream);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Properties getProp() {
        return prop;
    }
}