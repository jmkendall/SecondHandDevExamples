package junit_mockito_fest_propertySaver_example;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.List;

public class PropertySaver {

    private final PropertyAPI propertyAPI;
    private final Multimap<String, String> keyToValues = HashMultimap.create();

    public PropertySaver(PropertyAPI propertyAPI) {
        this.propertyAPI = propertyAPI;
    }

    public void addToProperty(String key, String value) {
        keyToValues.put(key, value);
    }

    public void commitProperties() {
        /*
            Some transformation or work done on values here.
         */
        for (String key : keyToValues.keys()) {
            List<String> values = Lists.newArrayList(keyToValues.get(key));
            propertyAPI.saveProperties(key, values);
        }
    }
}
