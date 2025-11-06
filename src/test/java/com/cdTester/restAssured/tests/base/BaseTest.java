package com.cdTester.restAssured.tests.base;

import com.cdTester.restAssured.config.ConfigManager;
import com.cdTester.restAssured.reporting.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.Assert;

import java.lang.reflect.Method;
import java.io.File;

public class BaseTest {
  protected static ExtentReports extent;
  protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
  protected static ConfigManager config;

  @BeforeSuite(alwaysRun = true)
  public void setUp() {
    // Create test-output directory if it doesn't exist
    File outputDir = new File("test-output");
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    extent = ExtentManager.createInstance("test-output/extent-report.html");
    System.out.println("ExtentReports initialized successfully");

    config = ConfigManager.getInstance();
  }

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod(Method method) {
    if (extent == null) {
      setUp(); // Initialize if not already done
    }

    String testName = method.getAnnotation(Test.class).description();
    String className = method.getDeclaringClass().getSimpleName();

    ExtentTest extentTest = extent.createTest(className + " - " + testName);
    test.set(extentTest);

    System.out.println("Starting test: " + testName);
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod(ITestResult result, Method method) {
    ExtentTest extentTest = test.get();
    extentTest.assignCategory(method.getAnnotation(Test.class).groups());

    String testName = method.getAnnotation(Test.class).description();
    System.out.println("Finished test: " + testName + ";  Result: " + test.get().getStatus() + "; Duration: " +  (result.getEndMillis() - result.getStartMillis())+ "ms");


//    if (extentTest != null) {
//      if (result.getStatus() == ITestResult.FAILURE) {
//        extentTest.fail("Test Failed: " + result.getThrowable());
//      } else if (result.getStatus() == ITestResult.SKIP) {
//        extentTest.skip("Test Skipped: " + result.getThrowable());
//      } else if (result.getStatus() == ITestResult.SUCCESS) {
//        extentTest.pass("Test Passed");
//      }
//  }

    // Clean up ThreadLocal to prevent memory leaks
    test.remove();
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() {
    if (extent != null) {
      extent.flush();
      System.out.println("ExtentReports report generated at: test-output/extent-report.html");
    }
  }

  public static ExtentTest addToExtentReport() {
    ExtentTest extentTest = test.get();
    if (extentTest == null) {
      System.err.println("WARNING: ExtentTest is null. Make sure @BeforeMethod is being called.");
    }
    return extentTest;
  }

  public static ExtentTest extentStep(String stepName) {
    return test.get().createNode(stepName);
  }

  /**
   * Assert equals with ExtentReport logging
   */
  public static <T> void extentAssertEquals(ExtentTest test, T actual, T expected, String errorMessage) {
    try {
      Assert.assertEquals(String.valueOf(actual), String.valueOf(expected), errorMessage);
      if (test != null) {
        test.pass("[✓] Assertion Passed: Expected actual value (" + actual + ") to EQUAL (" + expected + ")<br/>");
      }
    } catch (AssertionError e) {
      if (test != null) {
        test.fail("[✗] Assertion Failed: Expected actual value (" + actual + ") to EQUAL (" + expected + ")<br/>" + e.getMessage());
      }
      throw e;
    }
  }

  /**
   * Assert NOT equals with ExtentReport logging
   */
  public static <T> void extentAssertNotEquals(ExtentTest test, T actual, T expected, String errorMessage) {
    try {
      Assert.assertNotEquals(String.valueOf(actual), String.valueOf(expected), errorMessage);
      if (test != null) {
        test.pass("[✓] Assertion Passed: Expected actual value (" + actual + ") to NOT EQUAL (" + expected + ")<br/>");
      }
    } catch (AssertionError e) {
      if (test != null) {
        test.fail("[✗] Assertion Failed: Expected actual value (" + actual + ") to NOT EQUAL (" + expected + ")<br/>" + e.getMessage());
      }
      throw e;
    }
  }

  /**
   * Assert true with ExtentReport logging
   */
  public static void extentAssertTrue(ExtentTest test, boolean condition, String errorMessage) {
    try {
      Assert.assertTrue(condition, errorMessage);
      if (test != null) {
        test.pass("[✓] Assertion Passed: Expected condition to be TRUE<br/>" + errorMessage);
      }
    } catch (AssertionError e) {
      if (test != null) {
        test.fail("[✗] Assertion Failed: Expected condition to be TRUE<br/>" + e.getMessage() + "<br/>" + e.getMessage());
      }
      throw e;
    }
  }

  /**
   * Assert false with ExtentReport logging
   */
  public static void extentAssertFalse(ExtentTest test, boolean condition, String errorMessage) {
    try {
      Assert.assertFalse(condition, errorMessage);
      if (test != null) {
        test.pass("[✓] Assertion Passed: Expected condition to be FALSE<br/>" + errorMessage);
      }
    } catch (AssertionError e) {
      if (test != null) {
        test.fail("[✗] Assertion Failed: Expected condition to be FALSE<br/>" + e.getMessage() + "<br/>" + errorMessage);
      }
      throw e;
    }
  }

  /**
   * Assert not null with ExtentReport logging
   */
  public static void extentAssertNotNull(ExtentTest test, Object object, String errorMessage) {
    try {
      Assert.assertNotNull(object, errorMessage);
      if (test != null) {
        test.pass("[✓] Assertion Passed: Expected object to NOT be null <br/>" + errorMessage);
      }
    } catch (AssertionError e) {
      if (test != null) {
        test.fail("[✗] Assertion Failed: Expected object to NOT be null <br/>" + e.getMessage() + "<br/>" + object.toString());
      }
      throw e;
    }
  }

  /**
   * Assert null with ExtentReport logging
   */
  public static void extentAssertNull(ExtentTest test, Object object, String errorMessage) {
    try {
      Assert.assertNotNull(object, errorMessage);
      if (test != null) {
        test.pass("[✓] Assertion Passed: Expected object to be null <br/>" + object.toString());
      }
    } catch (AssertionError e) {
      if (test != null) {
        test.fail("[✗] Assertion Failed: Expected object to be null <br/>" + e.getMessage() + "<br/>" + object.toString());
      }
      throw e;
    }
  }

}