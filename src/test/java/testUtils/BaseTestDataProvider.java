package testUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseTestDataProvider {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static Object[][] loadTestData(String filePath) throws IOException {
    JsonNode rootNode = objectMapper.readTree(new File(filePath));
    List<Object[]> testData = new ArrayList<>();

    if (rootNode.isArray()) {
      Iterator<JsonNode> elements = rootNode.elements();
      while (elements.hasNext()) {
        JsonNode element = elements.next();
        testData.add(new Object[]{element});
      }
    }

    return testData.toArray(new Object[0][]);
  }
}