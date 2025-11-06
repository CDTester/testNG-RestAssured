package com.cdTester.restAssured.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestDataProvider {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @DataProvider(name = "userTestData")
  public static Object[][] getUserTestData() throws IOException {
    return loadTestData("src/test/resources/testdata/users.json");
  }

  @DataProvider(name = "productTestData")
  public static Object[][] getProductTestData() throws IOException {
    return loadTestData("src/test/resources/testdata/products.json");
  }

  private static Object[][] loadTestData(String filePath) throws IOException {
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