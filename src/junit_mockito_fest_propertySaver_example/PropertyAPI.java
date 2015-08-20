package junit_mockito_fest_propertySaver_example;

import java.util.List;

public interface PropertyAPI {

    void saveProperties(String key, List<String> properties);

    void saveProperties(int key, List<String> properties);

}
