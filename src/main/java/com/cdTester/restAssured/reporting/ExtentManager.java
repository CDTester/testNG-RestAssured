package com.cdTester.restAssured.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExtentManager {
  private static ExtentReports extent;

  public static ExtentReports createInstance(String fileName, XmlSuite suite) {
    ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);

    String suiteName = suite.getName();
    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    htmlReporter.config().setTheme(Theme.STANDARD);
    htmlReporter.config().setDocumentTitle("Extent Test Report");
    htmlReporter.config().setReportName(suiteName + " - Test Results");
    htmlReporter.config().setEncoding("utf-8");
    htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);

    extent.setSystemInfo("Suite Name", suite.getName());
    extent.setSystemInfo("Sets of tests in suite", String.valueOf(suite.getTests().size()));
    List<XmlTest> allTests = suite.getTests();
    StringBuilder tests = new StringBuilder();
    for (XmlTest test : allTests) {
      tests.append(test.getName()).append("<br/>");
    }
    extent.setSystemInfo("Tests", String.valueOf(tests));
    extent.setSystemInfo("Parallel Mode", suite.getParallel().toString());
    extent.setSystemInfo("Thread Count", String.valueOf(suite.getThreadCount()));
    extent.setSystemInfo("Execution Time", timestamp);


    extent.setSystemInfo("Framework", "TestNG + REST Assured");
    extent.setSystemInfo("Executed By", "QA Automation Engineer: " + System.getProperty("user.name"));
    extent.setSystemInfo("Build", suite.getParameter("build"));
    extent.setSystemInfo("OS", System.getProperty("os.name", "unknown"));
    extent.setSystemInfo("Environment", System.getProperty("env", "dev"));

    return extent;
  }

  public static ExtentReports getInstance() {
    return extent;
  }
}
