package com.cdTester.restAssured.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.cdTester.restAssured.config.ConfigManager;

public class ExtentManager {
  private static ExtentReports extent;

  public static ExtentReports createInstance(String fileName) {
    ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);

    htmlReporter.config().setTheme(Theme.STANDARD);
    htmlReporter.config().setDocumentTitle("API Test Report");
    htmlReporter.config().setReportName("API Automation Results");
    htmlReporter.config().setEncoding("utf-8");

    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);

    extent.setSystemInfo("Framework", "TestNG + REST Assured");
    extent.setSystemInfo("Author", "QA Automation Engineer: " + System.getProperty("user.name"));
    extent.setSystemInfo("Build", "1.0");
    extent.setSystemInfo("OS", System.getProperty("os.name", "unknown"));

    ConfigManager config = ConfigManager.getInstance();
    extent.setSystemInfo("Test Environment", config.getEnvironment());



    return extent;
  }

  public static ExtentReports getInstance() {
    return extent;
  }
}
