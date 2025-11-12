package com.cdTester.restAssured.config;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {
  private static ConfigManager instance;
  private Properties properties;

  private ConfigManager() {
    loadProperties();
  }

  public static ConfigManager getInstance() {
    if (instance == null) {
      instance = new ConfigManager();
    }
    return instance;
  }

  private void loadProperties() {
    properties = new Properties();
    try {
      String env = System.getProperty("env", "dev");
      String configFile = "src/test/resources/config/config-" + env + ".properties";
      System.out.println("Loading configuration for environment: " + env);
      FileInputStream fis = new FileInputStream(configFile);
      properties.load(fis);
      fis.close();
    } catch (IOException e) {
      throw new RuntimeException("Failed to load configuration: " + e.getMessage());
    }
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  public String getBaseUrl(String api) {
    String url = getProperty(api + ".base.url");
    if (url == null) {
      throw new RuntimeException("Base URL not found for API: " + api);
    }
    return url;
  }

  public String getAuthType(String api) {
    return getProperty(api + ".auth.type") == null ? "" : getProperty(api + ".auth.type");
  }

  public String getAuthKey(String api) {
    return getProperty(api + ".auth.key") == null ? "" : getProperty(api + ".auth.key");
  }

  public String getUsername(String api) {
    return getProperty(api + ".username") == null ? "" : getProperty(api + ".username");
  }

  public String getPassword(String api) {
    return getProperty(api + ".password") == null ? "" : getProperty(api + ".password");
  }

  public int getTimeout(String api) {
    return getProperty(api + ".timeout") == null ? 30 : Integer.parseInt(getProperty(api + ".timeout"));
  }

  public Map<String,String> getHeaders(String api) {
    Map<String,String> headers = new HashMap<>();
    String headerPrefix = api + ".headers.";

    // Iterate through all properties to find headers
    for (String key : properties.stringPropertyNames()) {
      if (key.startsWith(headerPrefix)) {
        String headerName = key.substring(headerPrefix.length());
        String headerValue = properties.getProperty(key);

        headers.put(headerName, headerValue);
      }
    }
    return headers;
  }



  // Get environment name
  public String getEnvironment() {
    return getProperty("environment", "unknown");
  }


  // Print current configuration (for debugging)
  public void printCurrentConfig(String api) {
    System.out.println("========================================");
    System.out.println("Current Configuration:");
    System.out.println("Environment: " + getEnvironment());
    System.out.println("Current API: " + api);
    System.out.println("Base URL: " + getBaseUrl(api));
    System.out.println("Auth Type: " + getAuthType(api));
    System.out.println("Timeout: " + getTimeout(api));
    System.out.println("========================================");
  }
}
