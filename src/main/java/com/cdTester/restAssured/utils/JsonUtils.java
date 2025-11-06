package com.cdTester.restAssured.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.List;

public class JsonUtils {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> T deserialize(Response response, Class<T> model) {
    try {
      return objectMapper.readValue(response.getBody().asString(), model);
    } catch (IOException e) {
      throw new RuntimeException("Failed to deserialize response: " + e.getMessage());
    }
  }

  /**
   * Deserialize response to a List of objects
   */
  public static <T> List<T> deserializeList(Response response, Class<T> clazz) {
    try {
      return objectMapper.readValue(
            response.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
      );
    } catch (IOException e) {
      throw new RuntimeException("Failed to deserialize response list: " + e.getMessage());
    }
  }


  /**
   * Deserialize using TypeReference (for complex generic types)
   */
  public static <T> T deserialize(Response response, TypeReference<T> typeReference) {
    try {
      return objectMapper.readValue(response.getBody().asString(), typeReference);
    } catch (IOException e) {
      throw new RuntimeException("Failed to deserialize response: " + e.getMessage());
    }
  }

  /**
   * Auto-detect if response is array or single object and deserialize accordingly
   */
  public static <T> Object deserializeAuto(Response response, Class<T> clazz) {
    try {
      String json = response.getBody().asString();
      JsonNode rootNode = objectMapper.readTree(json);

      if (rootNode.isArray()) {
        // It's an array, return List<T>
        return deserializeList(response, clazz);
      } else {
        // It's a single object, return T
        return deserialize(response, clazz);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to auto-deserialize response: " + e.getMessage());
    }
  }

  public static JsonNode parseJson(String json) {
    try {
      return objectMapper.readTree(json);
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse JSON: " + e.getMessage());
    }
  }

  public static String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (IOException e) {
      throw new RuntimeException("Failed to serialize object: " + e.getMessage());
    }
  }

  public static String toPrettyJson(Object object) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (IOException e) {
      throw new RuntimeException("Failed to serialize object: " + e.getMessage());
    }
  }

  public static String getJsonValue(Response response, String jsonPath) {
    return response.jsonPath().getString(jsonPath);
  }

  public static boolean isJsonArray(Response response) {
    try {
      JsonNode rootNode = objectMapper.readTree(response.getBody().asString());
      return rootNode.isArray();
    } catch (IOException e) {
      return false;
    }
  }

  public static boolean isJsonObject(Response response) {
    try {
      JsonNode rootNode = objectMapper.readTree(response.getBody().asString());
      return rootNode.isObject();
    } catch (IOException e) {
      return false;
    }
  }

  public static boolean validateJsonSchema(Response response, String schemaPath) {
    // Implementation for JSON schema validation
    // You can use libraries like json-schema-validator
    return true; // Placeholder
  }
}