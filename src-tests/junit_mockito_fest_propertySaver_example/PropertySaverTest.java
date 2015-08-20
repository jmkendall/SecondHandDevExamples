package junit_mockito_fest_propertySaver_example;


import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class PropertySaverTest {

    private static final String PROPERTY_VALUE_ONE = "a property";
    private static final String PROPERTY_VALUE_TWO = "a second property";
    private static final String PROPERTY_VALUE_THREE = "a third property";

    @InjectMocks
    private final PropertyAPI mockAPI = Mockito.mock(PropertyAPI.class);

    private PropertySaver propertySaver;
    private List<String> submittedToAPI = Lists.newArrayList();
    private String testKeySubmitted = "";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        propertySaver = new PropertySaver(mockAPI);
        Mockito.doAnswer(invocation -> {
            Object submittedKey = invocation.getArguments()[0];
            Object submittedValues = invocation.getArguments()[1];
            testKeySubmitted = (String) submittedKey;
            submittedToAPI = (List<String>) submittedValues;
            return null;
        }).when(mockAPI).saveProperties(Mockito.anyString(), Mockito.anyListOf(String.class));

        // Flag use of the API call we want to stay away from.
        Mockito.doThrow(new RuntimeException("saveProperties(String, List<String>) should be used."))
                .when(mockAPI).saveProperties(Mockito.anyInt(), Mockito.anyListOf(String.class));

    }

    @Test
    public void successfulCommitPropertiesCallMade_TasksAddedInOrder() {
        List<String> expectedValues = Lists.newArrayList(PROPERTY_VALUE_ONE, PROPERTY_VALUE_TWO, PROPERTY_VALUE_THREE);
        String testKey = "myTestKey";

        propertySaver.addToProperty(testKey, PROPERTY_VALUE_ONE);
        propertySaver.addToProperty(testKey, PROPERTY_VALUE_TWO);
        propertySaver.addToProperty(testKey, PROPERTY_VALUE_THREE);
        propertySaver.commitProperties();

        Assert.assertEquals(testKey, testKeySubmitted);
        Assertions.assertThat(submittedToAPI).hasSameSizeAs(expectedValues).containsAll(expectedValues);
    }

    @Test
    public void successfulCommitPropertiesCallMade_TasksAddedOutOfOrder() {
        List<String> expectedValues = Lists.newArrayList(PROPERTY_VALUE_ONE, PROPERTY_VALUE_THREE, PROPERTY_VALUE_TWO);
        String testKey = "myTestKey";

        propertySaver.addToProperty(testKey, PROPERTY_VALUE_THREE);
        propertySaver.addToProperty(testKey, PROPERTY_VALUE_TWO);
        propertySaver.addToProperty(testKey, PROPERTY_VALUE_ONE);
        propertySaver.commitProperties();

        Assert.assertEquals(testKey, testKeySubmitted);
        Assertions.assertThat(submittedToAPI).hasSameSizeAs(expectedValues).containsAll(expectedValues);
    }

}